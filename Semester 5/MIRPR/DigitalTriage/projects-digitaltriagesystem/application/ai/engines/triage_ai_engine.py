import numpy as np
import joblib
import xgboost as xgb

class TriageAIEngineXGB:
    def __init__(self, model_path="ai/xgb_model.json", symptoms_path="utils/symptoms_list.npy", label_path="utils/label_encoder.pkl"):
        self.model = xgb.XGBClassifier()
        self.model.load_model(model_path)

        self.all_symptoms = np.load(symptoms_path, allow_pickle=True).tolist()
        self.label_encoder = joblib.load(label_path)

    def predict_disease(self, symptoms_list):
        input_data = np.zeros(len(self.all_symptoms))
        for s in symptoms_list:
            s_clean = s.strip().lower()
            if s_clean in self.all_symptoms:
                input_data[self.all_symptoms.index(s_clean)] = 1

        probs = self.model.predict_proba([input_data])[0]
        pred_idx = np.argmax(probs)
        disease = self.label_encoder.inverse_transform([pred_idx])[0]
        confidence = probs[pred_idx] * 100

        # Top 3 suggestions
        top3_idx = np.argsort(probs)[::-1][:3]
        top3 = [(self.label_encoder.inverse_transform([i])[0], probs[i]*100) for i in top3_idx]

        return {
            "disease": disease,
            "confidence": round(confidence, 1),
            "alternatives": top3[1:]
        }
