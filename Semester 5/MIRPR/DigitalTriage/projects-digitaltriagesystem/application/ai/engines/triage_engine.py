class TriageEngine:
    def __init__(self):
        self.symptoms_db = {
            'fever': {'weight': 3, 'conditions': ['infection', 'flu', 'covid']},
            'cough': {'weight': 2, 'conditions': ['flu', 'covid', 'bronchitis']},
            'headache': {'weight': 2, 'conditions': ['migraine', 'tension', 'flu']},
            'chest_pain': {'weight': 5, 'conditions': ['cardiac', 'anxiety', 'respiratory']},
            'shortness_of_breath': {'weight': 5, 'conditions': ['cardiac', 'respiratory', 'anxiety']},
            'nausea': {'weight': 2, 'conditions': ['gastro', 'infection', 'migraine']},
            'dizziness': {'weight': 3, 'conditions': ['cardiac', 'neurological', 'dehydration']},
            'fatigue': {'weight': 1, 'conditions': ['anemia', 'depression', 'chronic_fatigue']},
            'anxiety': {'weight': 2, 'conditions': ['anxiety_disorder', 'panic', 'stress']},
            'depression': {'weight': 2, 'conditions': ['depression', 'bipolar', 'adjustment']},
            'abdominal_pain': {'weight': 3, 'conditions': ['gastro', 'appendicitis', 'ibs']},
            'rash': {'weight': 2, 'conditions': ['allergy', 'infection', 'dermatitis']},
        }

        self.urgency_rules = {
            'chest_pain': 'emergency',
            'shortness_of_breath': 'emergency',
            'severe_bleeding': 'emergency',
        }

        self.provider_mapping = {
            'cardiac': 'Cardiologist',
            'respiratory': 'Pulmonologist',
            'neurological': 'Neurologist',
            'gastro': 'Gastroenterologist',
            'anxiety_disorder': 'Mental Health Professional',
            'depression': 'Mental Health Professional',
            'panic': 'Mental Health Professional',
            'allergy': 'Allergist',
            'dermatitis': 'Dermatologist',
            'infection': 'General Practitioner',
            'flu': 'General Practitioner',
            'default': 'General Practitioner'
        }

    def analyze_symptoms(self, symptoms, severity, age=0, medical_history=""):
        if not symptoms:
            return None

        total_weight = 0
        condition_scores = {}
        urgency = 'routine'

        for symptom in symptoms:
            symptom_key = symptom.lower().replace(' ', '_')

            if symptom_key in self.urgency_rules:
                urgency = self.urgency_rules[symptom_key]

            if symptom_key in self.symptoms_db:
                symptom_data = self.symptoms_db[symptom_key]
                weight = symptom_data['weight'] * severity
                total_weight += weight

                for condition in symptom_data['conditions']:
                    if condition not in condition_scores:
                        condition_scores[condition] = 0
                    condition_scores[condition] += weight

        if not condition_scores:
            return None

        sorted_conditions = sorted(condition_scores.items(), key=lambda x: x[1], reverse=True)
        top_condition = sorted_conditions[0][0]

        provider = self.provider_mapping.get(top_condition, self.provider_mapping['default'])

        confidence = min((sorted_conditions[0][1] / max(total_weight, 1)) * 100, 95)

        return {
            'primary_condition': top_condition.replace('_', ' ').title(),
            'provider': provider,
            'urgency': urgency,
            'confidence': round(confidence, 1),
            'alternative_conditions': [c[0].replace('_', ' ').title() for c in sorted_conditions[1:3]]
        }

    def get_symptom_list(self):
        return [s.replace('_', ' ').title() for s in self.symptoms_db.keys()]
