import re
import pandas as pd
import torch
import torch.nn.functional as F
from sklearn.feature_extraction._stop_words import ENGLISH_STOP_WORDS

# inference function for BioBERT
def get_specialist(patient_text, model, tokenizer, device, threshold=0.45):
    """
    Generate a prediction based on a given prompt
    Input: Patient's text
    Output: Recommended specialist and confidence for him or ask human help
    """
    negation_words = {'no', 'not', 'nor', 'neither', 'never', 'denies', 'without'}
    SKLEARN_STOPWORDS = set(ENGLISH_STOP_WORDS) - negation_words

    medical_stopwords = {
        'pt', 'patient', 'hx', 'ros', 'admitted', 'hospital',  # Am scos 'denies'
        'history', 'illness', 'presenting', 'states', 'reportedly', 'due',
        'mg', 'capsule', 'tablet', 'solution', 'drops', 'daily', 'tid', 'bid',
        'po', 'iv', 'unit', 'vitals', 'flow',
        'rr', 'bp', 'temp', 'pulse', 'w', 'h', 'm', 'f', 'o', 'r', 'q', 'g',
        'cc', 'cxr', 'us', 'chief'
    }
    ALL_STOPWORDS = SKLEARN_STOPWORDS.union(medical_stopwords)

    def clean_text(text, stop_words=ALL_STOPWORDS):
        if pd.isna(text) or text is None:
            return ""

        text = str(text).lower()
        text = re.sub(r'(\s+in\s+)|(\s+at\s+)|(sometime\s+in\s+)|(\s*\d{4}\s*)|(\s*ms\.\s*,\s*)', ' ', text)
        text = re.sub(r'[^a-zA-Z0-9\s]', ' ', text)

        tokens = text.split()
        filtered_tokens = [token for token in tokens if token not in stop_words and len(token) > 1]

        return " ".join(filtered_tokens)

    model.eval()
    text_clean = clean_text(patient_text)

    # 2. Tokenization
    inputs = tokenizer(
        text_clean,
        return_tensors="pt",
        truncation=True,
        max_length=512,
        padding=True
    ).to(device)

    # 3. Prediction
    with torch.no_grad():
        outputs = model(**inputs)
        logits = outputs.logits

    # 4. Compute probabilities (Softmax)
    probs = F.softmax(logits, dim=-1)
    max_prob, predicted_class_id = torch.max(probs, dim=-1)

    score = max_prob.item()
    predicted_label = model.config.id2label[predicted_class_id.item()]

    # 5. If the model is not sure enough, we prefer to let humans decide the specialist needed
    if score < threshold:
        return {
            "specialist": "Human triage is needed!",
            "reason": "Model is not confident",
            "confidence": score,
            "raw_prediction": predicted_label
        }

    # 6. Return the specialist and confidence
    return {
        "specialist": predicted_label,
        "confidence": score
    }

# inference function for PubMedBERT
def get_advice(patient_text, model, tokenizer, device):
    '''
    Generate medical advice based on a given prompt by patient
    :Input: Patient's text
    :return: Medical advice
    '''

    def clean_text(text):
        """Aplică aceeași curățare de bază ca în scriptul de antrenare."""
        if not isinstance(text, str):
            return ""
        text = re.sub(r'\s+', ' ', text)
        text = re.sub(r'[^a-zA-Z0-9.,;:()\- ]', '', text)
        return text.strip()

    model.eval()
    text_clean = clean_text(patient_text)

    inputs = tokenizer(
        text_clean,
        return_tensors="pt",
        truncation=True,
        max_length=512,
        padding=True
    ).to(device)

    input_ids = inputs['input_ids'].to(device)
    attention_mask = inputs['attention_mask'].to(device)

    # 3.4. Rularea Predicției și Calcularea Probabilităților (MODIFICAT AICI)
    with torch.no_grad():
        outputs = model(input_ids, attention_mask=attention_mask)
        logits = outputs.logits

        # Aplică Softmax pentru a obține probabilitățile (suma = 1.0)
        probabilities = torch.softmax(logits, dim=-1)[0]

        # Găsește indexul clasei cu probabilitatea cea mai mare
        predicted_class_id = torch.argmax(probabilities).item()

    # 3.5. Afișarea Rezultatului
    predicted_label = model.config.id2label.get(predicted_class_id, "Etichetă necunoscută")

    return {
        'medical_advice': predicted_label,
        'confidence': probabilities[predicted_class_id].item()*100
    }
