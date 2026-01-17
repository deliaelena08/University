import React, { useState, useRef, useEffect } from 'react';
import { Send, User, Bot, Activity, FileText, AlertCircle, RefreshCw } from 'lucide-react';

const API_ENDPOINT = 'http://localhost:8000/predict';

const QUESTIONS = [
    {
        id: 'complaint',
        text: "Hello. I am the AI Triage Assistant. What is the main reason for your visit today?",
        placeholder: "e.g., Chest pain, severe headache..."
    },
    {
        id: 'age',
        text: "Thank you. How old are you?",
        placeholder: "e.g., 45"
    },
    {
        id: 'gender',
        text: "What is your gender?",
        placeholder: "e.g., Male, Female"
    },
    {
        id: 'history_pmh',
        text: "Do you have any past medical history we should know about? (e.g., hypertension, diabetes)",
        placeholder: "e.g., High blood pressure, asthma..."
    },
    {
        id: 'history_social',
        text: "Do you smoke or use alcohol?",
        placeholder: "e.g., Smoker for 10 years, social drinker"
    },
    {
        id: 'history_details',
        text: "Please describe your current symptoms in more detail. When did they start?",
        placeholder: "e.g., Started 2 hours ago, radiating to left arm..."
    }
];

export default function App() {
    const [messages, setMessages] = useState([]);
    const [currentStep, setCurrentStep] = useState(0);
    const [answers, setAnswers] = useState({});
    const [inputText, setInputText] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [prediction, setPrediction] = useState(null);
    const messagesEndRef = useRef(null);

    const initialMessageRef = useRef(false);

    useEffect(() => {
        if (!initialMessageRef.current) {
            addBotMessage(QUESTIONS[0].text);
            initialMessageRef.current = true;
        }
    }, []);

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages, isLoading]);

    const addBotMessage = (text) => {
        setMessages(prev => [...prev, { sender: 'bot', text }]);
    };

    const addUserMessage = (text) => {
        setMessages(prev => [...prev, { sender: 'user', text }]);
    };

    const handleSend = async () => {
        if (!inputText.trim()) return;

        const currentQuestion = QUESTIONS[currentStep];
        const userResponse = inputText.trim();

        addUserMessage(userResponse);
        setInputText("");

        const newAnswers = { ...answers, [currentQuestion.id]: userResponse };
        setAnswers(newAnswers);

        if (currentStep < QUESTIONS.length - 1) {
            setTimeout(() => {
                setCurrentStep(prev => prev + 1);
                addBotMessage(QUESTIONS[currentStep + 1].text);
            }, 500);
        } else {
            setIsLoading(true);
            await processAndSubmit(newAnswers);
        }
    };

    const processAndSubmit = async (finalAnswers) => {
        const historyString = `${finalAnswers.age}-year-old ${finalAnswers.gender} with a history of ${finalAnswers.history_pmh}. Social history: ${finalAnswers.history_social}. Presenting details: ${finalAnswers.history_details}.`;

        const payload = {
            complaint: finalAnswers.complaint,
            history: historyString
        };

        addBotMessage("Thank you. I am analyzing your case now...");

        try {
            // --- REAL SERVER CALL ---
            const response = await fetch(API_ENDPOINT, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(payload)
            });

            if (!response.ok) {
                throw new Error(`HTTP Error: ${response.status}`);
            }

            const data = await response.json();

            // Setam predictia direct cu datele de la server
            // Serverul trebuie sa returneze un JSON de forma: { "prediction": "Home", "confidence": 0.85, ... }
            setPrediction(data);

        } catch (error) {
            addBotMessage("Error: Could not connect to the triage server. Please check if the backend is running.");
            console.error("Fetch error:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const resetChat = () => {
        setMessages([]);
        setAnswers({});
        setCurrentStep(0);
        setPrediction(null);
        setInputText("");
        setTimeout(() => {
            addBotMessage(QUESTIONS[0].text);
        }, 10);
    };

    return (
        <div className="flex w-full flex-col h-screen bg-slate-50 font-sans text-slate-800">

            <header className="bg-blue-700 text-white p-4 shadow-md flex items-center justify-between">
                <div className="flex items-center gap-2">
                    <Activity className="h-6 w-6" />
                    <h1 className="text-xl font-bold tracking-tight">Digital Triage Assistant</h1>
                </div>
                <button
                    onClick={resetChat}
                    className="p-2 hover:bg-blue-600 rounded-full transition-colors"
                    title="Restart Triage"
                >
                    <RefreshCw className="h-5 w-5" />
                </button>
            </header>

            <div className="flex-1 overflow-y-auto p-4 space-y-4">
                {messages.map((msg, index) => (
                    <div
                        key={index}
                        className={`flex ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}
                    >
                        <div className={`flex w-full ${msg.sender === 'user' ? 'justify-end' : 'justify-start'}`}>
                            <div className={`flex max-w-[95%] md:max-w-[70%] ${msg.sender === 'user' ? 'flex-row-reverse' : 'flex-row'} items-end gap-2`}>
                                <div className={`h-8 w-8 rounded-full flex items-center justify-center shrink-0 ${msg.sender === 'user' ? 'bg-blue-600 text-white' : 'bg-emerald-600 text-white'}`}>
                                    {msg.sender === 'user' ? <User size={16} /> : <Bot size={16} />}
                                </div>

                                <div className={`p-3 rounded-2xl shadow-sm text-sm md:text-base leading-relaxed ${
                                    msg.sender === 'user'
                                        ? 'bg-blue-600 text-white rounded-br-none'
                                        : 'bg-white border border-slate-200 rounded-bl-none'
                                }`}>
                                    {msg.text}
                                </div>
                            </div>
                        </div>
                    </div>
                ))}

                {isLoading && (
                    <div className="flex justify-start">
                        <div className="flex items-center gap-2">
                            <div className="h-8 w-8 rounded-full bg-emerald-600 flex items-center justify-center">
                                <Bot size={16} className="text-white animate-pulse" />
                            </div>
                            <div className="bg-white border border-slate-200 p-3 rounded-2xl rounded-bl-none shadow-sm flex gap-1">
                                <span className="w-2 h-2 bg-slate-400 rounded-full animate-bounce" style={{ animationDelay: '0ms' }}></span>
                                <span className="w-2 h-2 bg-slate-400 rounded-full animate-bounce" style={{ animationDelay: '150ms' }}></span>
                                <span className="w-2 h-2 bg-slate-400 rounded-full animate-bounce" style={{ animationDelay: '300ms' }}></span>
                            </div>
                        </div>
                    </div>
                )}

                {prediction && (
                    <div className="flex justify-center my-6 animate-fade-in">
                        <div className="bg-white border border-slate-200 rounded-xl shadow-lg w-full max-w-md overflow-hidden">
                            <div className="bg-slate-800 text-white p-3 flex items-center gap-2">
                                <FileText size={18} />
                                <span className="font-semibold">Triage Assessment</span>
                            </div>
                            <div className="p-4 space-y-4">
                                <div className="text-xs text-slate-500 uppercase font-bold tracking-wider mb-1">Generated Clinical Note</div>
                                <div className="bg-slate-100 p-3 rounded text-xs text-slate-700 font-mono mb-4">
                                    <p><span className="font-bold">CC:</span> {answers.complaint}</p>
                                    <p className="mt-1"><span className="font-bold">HPI:</span> {answers.age}-year-old {answers.gender}. PMH: {answers.history_pmh}. SocHx: {answers.history_social}. {answers.history_details}</p>
                                </div>

                                <div className="flex items-center justify-between p-4 bg-blue-50 rounded-lg border border-blue-100">
                                    <div>
                                        <div className="text-sm text-blue-600 font-medium">Recommended Disposition</div>
                                        {/* Aici folosim prediction.prediction conform structurii JSON */}
                                        <div className="text-2xl font-bold text-slate-900">{prediction.prediction}</div>
                                    </div>
                                    <div className="text-right">
                                        <div className="text-sm text-blue-600 font-medium">Confidence</div>
                                        <div className="text-xl font-bold text-slate-900">{(prediction.confidence * 100).toFixed(0)}%</div>
                                    </div>
                                </div>

                                {prediction.prediction !== 'Home' && (
                                    <div className="flex items-start gap-2 text-amber-700 bg-amber-50 p-3 rounded text-sm">
                                        <AlertCircle size={18} className="shrink-0 mt-0.5" />
                                        <p>This patient may require facility care. Please review vital signs and consult attending physician.</p>
                                    </div>
                                )}
                            </div>

                            <div className="bg-slate-50 p-3 text-center border-t border-slate-100">
                                <button
                                    onClick={resetChat}
                                    className="text-blue-600 text-sm font-semibold hover:underline"
                                >
                                    Start New Triage Case
                                </button>
                            </div>
                        </div>
                    </div>
                )}

                <div ref={messagesEndRef} />
            </div>

            <div className="bg-white p-4 border-t border-slate-200">
                <div className="max-w-4xl mx-auto flex gap-2">
                    <input
                        type="text"
                        value={inputText}
                        onChange={(e) => setInputText(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && !isLoading && !prediction && handleSend()}
                        disabled={isLoading || !!prediction}
                        placeholder={prediction ? "Reset chat to start over" : QUESTIONS[currentStep]?.placeholder || "Type your answer..."}
                        className="flex-1 px-4 py-3 bg-slate-100 border-0 rounded-xl focus:ring-2 focus:ring-blue-500 focus:bg-white transition-all outline-none disabled:opacity-50"
                    />
                    <button
                        onClick={handleSend}
                        disabled={isLoading || !!prediction || !inputText.trim()}
                        className="bg-blue-600 hover:bg-blue-700 text-white p-3 rounded-xl transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center min-w-[3rem]"
                    >
                        <Send size={20} />
                    </button>
                </div>
                <div className="max-w-4xl mx-auto mt-2 text-center">
                    <p className="text-xs text-slate-400">
                        AI Triage Assistant v1.0 • Not a substitute for professional medical advice.
                    </p>
                </div>
            </div>
        </div>
    );
}