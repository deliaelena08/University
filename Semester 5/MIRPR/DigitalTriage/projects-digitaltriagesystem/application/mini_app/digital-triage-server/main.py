from contextlib import asynccontextmanager
from transformers import AutoModelForSequenceClassification, AutoTokenizer
import torch
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from inference import get_specialist, get_advice


class TextInput(BaseModel):
    complaint: str
    history: str


class TextOutput(BaseModel):
    prediction: str
    confidence: float
    debug_info: str = None


models = {}


@asynccontextmanager
async def lifespan(app: FastAPI):
    global models

    BIOBERT_PATH = './models/biobert'
    PUBMEDBERT_PATH = './models/pubmedbert'
    PUBMEDBERT_TOKENIZER_PATH = "microsoft/BiomedNLP-PubMedBERT-base-uncased-abstract-fulltext"

    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

    try:
        print("Loading models... (this might take a moment)")
        models['biobert'] = {
            'tokenizer': AutoTokenizer.from_pretrained(BIOBERT_PATH),
            'model': AutoModelForSequenceClassification.from_pretrained(BIOBERT_PATH).to(device),
            'device': device,
        }
        models['pubmedbert'] = {
            'tokenizer': AutoTokenizer.from_pretrained(PUBMEDBERT_TOKENIZER_PATH),
            'model': AutoModelForSequenceClassification.from_pretrained(PUBMEDBERT_PATH).to(device),
            'device': device,
        }
        print('Models loaded successfully!')

    except Exception as e:
        print(f'Error at loading the models: {e}')

    yield

    print('Clean up the models...')
    models.clear()


app = FastAPI(lifespan=lifespan)

origins = [
    "http://localhost:5173",
    "http://localhost:3000",
    "http://127.0.0.1:5173",
    "*"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.post('/predict', response_model=TextOutput)
def predict(text: TextInput):
    full_prompt = f"{text.complaint}. {text.history}"

    print(f"Analyzing: {full_prompt}")

    prediction_bert = get_specialist(full_prompt, models['biobert']['model'], models['biobert']['tokenizer'],
                                     models['biobert']['device'])
    prediction_pubmed = get_advice(full_prompt, models['pubmedbert']['model'], models['pubmedbert']['tokenizer'],
                                   models['pubmedbert']['device'])

    medical_advice = prediction_pubmed['medical_advice']
    specialist = prediction_bert['specialist']
    conf_bert = prediction_bert['confidence']
    conf_pubmed = prediction_pubmed['confidence']

    final_prediction = ""

    if medical_advice == 'Home':
        final_prediction = 'Your problem does not put you in danger. You can go home.'
        confidence = conf_bert

    elif medical_advice == 'Home With Service Facility':
        final_prediction = 'Your problem does not put you in danger. You can go home, but you need service facility.'
        confidence = conf_bert

    elif medical_advice == 'Extended Care Facility':
        if specialist != 'Human triage is needed!':
            final_prediction = f'You need to be seen by a {specialist}.'
            confidence = conf_bert
        else:
            final_prediction = 'You need medical help, but I cannot recommend a specialist. Human triage is needed!'
            confidence = conf_pubmed

    else:
        final_prediction = 'This is an expired case!'
        confidence = conf_pubmed

    return TextOutput(
        prediction=final_prediction,
        confidence=confidence,
        debug_info=f"Advice: {medical_advice} | Specialist: {specialist}"
    )