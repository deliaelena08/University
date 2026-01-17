from kivymd.app import MDApp
from kivymd.uix.screen import MDScreen
from kivymd.uix.boxlayout import MDBoxLayout
from kivymd.uix.label import MDLabel
from kivymd.uix.selectioncontrol import MDCheckbox
from kivymd.uix.slider import MDSlider
from kivy.uix.scrollview import ScrollView
from kivy.metrics import dp

from application.ai.engines.triage_engine import TriageEngine
from kivymd.uix.textfield import MDTextField
from kivymd.uix.button import MDRaisedButton


class TriageApp(MDApp):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.engine = TriageEngine()
        self.selected_symptoms = set()
        self.severity = 5
        self.user_name = ""
        self.age = 0
        self.medical_history = ""

    def build(self):
        self.theme_cls.primary_palette = "Blue"
        self.theme_cls.theme_style = "Light"

        self.screen = MDScreen()

        scroll_main = ScrollView(size_hint=(1, 1))

        self.main_layout = MDBoxLayout(
            orientation='vertical',
            padding=dp(20),
            spacing=dp(10),
            size_hint_y=None
        )
        self.main_layout.bind(minimum_height=self.main_layout.setter('height'))

        self.build_header()
        self.build_personal_info()
        self.build_symptom_selection()
        self.build_severity_selector()
        self.build_analyze_button()
        self.build_result_area()

        scroll_main.add_widget(self.main_layout)
        self.screen.add_widget(scroll_main)
        return self.screen

    def build_header(self):
        header = MDLabel(
            text="Digital Triage System",
            font_style="H5",
            halign="center",
            size_hint_y=None,
            height=dp(50)
        )
        self.main_layout.add_widget(header)

    def build_personal_info(self):
        info_label = MDLabel(
            text="Personal Information",
            font_style="Subtitle1",
            size_hint_y=None,
            height=dp(35)
        )
        self.main_layout.add_widget(info_label)

        self.name_field = MDTextField(
            hint_text="Name",
            size_hint_y=None,
            height=dp(50)
        )
        self.main_layout.add_widget(self.name_field)

        self.age_field = MDTextField(
            hint_text="Age",
            size_hint_y=None,
            height=dp(50),
            input_filter="int"
        )
        self.main_layout.add_widget(self.age_field)

        self.history_field = MDTextField(
            hint_text="Medical History (optional)",
            size_hint_y=None,
            height=dp(70),
            multiline=True
        )
        self.main_layout.add_widget(self.history_field)

    def build_symptom_selection(self):
        symptom_label = MDLabel(
            text="Select Symptoms:",
            font_style="Subtitle1",
            size_hint_y=None,
            height=dp(35)
        )
        self.main_layout.add_widget(symptom_label)

        symptom_container = MDBoxLayout(
            orientation='vertical',
            spacing=dp(5),
            size_hint_y=None,
            padding=dp(5)
        )
        symptom_container.bind(minimum_height=symptom_container.setter('height'))

        self.checkboxes = {}
        for symptom in self.engine.get_symptom_list():
            row = MDBoxLayout(
                orientation='horizontal',
                size_hint_y=None,
                height=dp(40),
                spacing=dp(10)
            )

            checkbox = MDCheckbox(
                size_hint=(None, None),
                size=(dp(35), dp(35))
            )
            symptom_key = symptom.lower().replace(' ', '_')
            checkbox.bind(active=lambda cb, active, s=symptom_key: self.on_symptom_toggle(s, active))
            self.checkboxes[symptom_key] = checkbox

            label = MDLabel(
                text=symptom,
                size_hint_x=0.8
            )

            row.add_widget(checkbox)
            row.add_widget(label)
            symptom_container.add_widget(row)

        self.main_layout.add_widget(symptom_container)

    def build_severity_selector(self):
        self.severity_label = MDLabel(
            text="Severity Level: 5",
            font_style="Subtitle1",
            size_hint_y=None,
            height=dp(35)
        )
        self.main_layout.add_widget(self.severity_label)

        slider = MDSlider(
            min=1,
            max=10,
            value=5,
            size_hint_y=None,
            height=dp(40)
        )
        slider.bind(value=self.on_severity_change)
        self.main_layout.add_widget(slider)

    def build_analyze_button(self):
        analyze_button = MDRaisedButton(
            text="Analyze Symptoms",
            size_hint=(1, None),
            height=dp(50),
            pos_hint={'center_x': 0.5}
        )
        analyze_button.bind(on_release=self.analyze_symptoms)
        self.main_layout.add_widget(analyze_button)

    def build_result_area(self):
        self.result_label = MDLabel(
            text="",
            halign="center",
            font_style="Body1",
            markup=True,
            size_hint_y=None,
            height=dp(150)
        )
        self.main_layout.add_widget(self.result_label)

    def on_symptom_toggle(self, symptom, active):
        if active:
            self.selected_symptoms.add(symptom)
        else:
            self.selected_symptoms.discard(symptom)

    def on_severity_change(self, instance, value):
        self.severity = int(value)
        self.severity_label.text = f"Severity Level: {self.severity}"

    def analyze_symptoms(self, instance):
        self.user_name = self.name_field.text
        self.age = int(self.age_field.text) if self.age_field.text else 0
        self.medical_history = self.history_field.text

        if not self.user_name:
            self.result_label.text = "[b]Please enter your name[/b]"
            return

        if not self.selected_symptoms:
            self.result_label.text = "[b]Please select at least one symptom[/b]"
            return

        result = self.engine.analyze_symptoms(
            list(self.selected_symptoms),
            self.severity,
            self.age,
            self.medical_history
        )

        if result:
            urgency_color = {
                'emergency': '[color=#ff0000]',
                'urgent': '[color=#ff9800]',
                'routine': '[color=#4caf50]'
            }

            color = urgency_color.get(result['urgency'], '[color=#4caf50]')

            output = f"[b]{self.user_name}'s Assessment[/b]\n\n"
            output += f"[b]Primary Indication:[/b] {result['primary_condition']}\n"
            output += f"[b]Recommended Provider:[/b] {result['provider']}\n"
            output += f"[b]Urgency:[/b] {color}{result['urgency'].upper()}[/color]\n"
            output += f"[b]Confidence:[/b] {result['confidence']}%\n"

            if result['alternative_conditions']:
                output += f"\n[b]Also Consider:[/b] {', '.join(result['alternative_conditions'])}"

            self.result_label.text = output
        else:
            self.result_label.text = "[b]Unable to analyze. Please try again.[/b]"


if __name__ == '__main__':
    TriageApp().run()
