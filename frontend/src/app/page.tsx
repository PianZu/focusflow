// src/app/page.tsx (Beispiel für App Router)

"use client"; // Da wir fetch im Client verwenden und State managen
import {
    UserLoginData,
    UserRegistrationData,
    UserProfile,
    loginUser,
    registerUser,
    // getUserById, // Könnte nützlich sein, um Profildaten nach Login zu holen
} from '@/services/userService'; // Pfad korrigiert für Konsistenz
import { useEffect, useState } from 'react';

export default function MainPage() {
    // Authentifizierungs-State
    const [authUser, setAuthUser] = useState<UserProfile | null>(null); // Angemeldeter Benutzer
    const [isAuthLoading, setIsAuthLoading] = useState(false);
    const [authError, setAuthError] = useState<string | null>(null);
    const [showRegisterForm, setShowRegisterForm] = useState(false);

    // Login Form State
    const [loginEmail, setLoginEmail] = useState('');
    const [loginPassword, setLoginPassword] = useState('');

    // Registrierungs Form State
    const [registerEmail, setRegisterEmail] = useState('');
    const [registerPassword, setRegisterPassword] = useState('');
    const [registerPasswordConfirm, setRegisterPasswordConfirm] = useState('');
    const [registerFirstName, setRegisterFirstName] = useState('');
    const [registerLastName, setRegisterLastName] = useState('');

    // Effekt, um z.B. zu prüfen, ob der Benutzer bereits eingeloggt ist (z.B. durch ein Token im localStorage)
    useEffect(() => {
        // Hier könntest du Logik einfügen, um einen bestehenden Login-Status zu prüfen
        // z.B. wenn du ein Token im localStorage speicherst.
        // const token = localStorage.getItem('authToken');
        // if (token) { /* Benutzer validieren und authUser setzen */ }
    }, []);

    const handleLoginSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsAuthLoading(true);
        setAuthError(null);
        const credentials: UserLoginData = { email: loginEmail, password_P: loginPassword };
        try {
            const result = await loginUser(credentials);
            if (result.success) {
                // Annahme: Nach erfolgreichem Login setzen wir den Benutzer.
                // In einer echten App würdest du hier vielleicht das User-Objekt vom Backend bekommen
                // oder es basierend auf der E-Mail/Token abrufen.
                // Für dieses Beispiel simulieren wir ein User-Objekt.
                // Dein Backend /api/user/login gibt aktuell nur eine Textnachricht zurück.
                // Du müsstest es erweitern, um User-Daten oder ein Token zurückzugeben.
                // Fürs Erste:
                alert("Login erfolgreich: " + result.message + "\nNavigiere nun zu Tasks oder Teams.");
                // Hier müsstest du die User ID bekommen. Da dein Login-Endpunkt keine User-ID zurückgibt,
                // ist dies ein Platzhalter. Du könntest getUserById nach dem Login aufrufen,
                // wenn du die ID nicht direkt vom Login-Endpunkt erhältst.
                // Für dieses Beispiel nehmen wir an, die E-Mail ist eindeutig und wir verwenden eine Dummy-ID.
                // Besser wäre, wenn dein Login-Endpunkt die UserProfile-Daten zurückgibt.
                setAuthUser({ id: Date.now(), email: loginEmail, firstName: '', lastName: '', role: 'USER' }); // Platzhalter ID
                setLoginEmail('');
                setLoginPassword('');
            } else {
                setAuthError(result.message || "Login fehlgeschlagen.");
            }
        } catch (err: any) {
            setAuthError(err.message || "Ein Fehler ist aufgetreten.");
        } finally {
            setIsAuthLoading(false);
        }
    };

    const handleRegisterSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (registerPassword !== registerPasswordConfirm) {
            setAuthError("Passwörter stimmen nicht überein.");
            return;
        }
        setIsAuthLoading(true);
        setAuthError(null);
        const registrationData: UserRegistrationData = {
            email: registerEmail,
            password_P: registerPassword,
            passwordConfirm: registerPasswordConfirm,
            firstName: registerFirstName,
            lastName: registerLastName,
        };
        try {
            const result = await registerUser(registrationData);
            if (result.success) {
                alert(result.message); // z.B. "Registrierung erfolgreich. Bitte einloggen."
                setShowRegisterForm(false); // Zurück zum Login-Formular
                // Formularfelder leeren
                setRegisterEmail('');
                setRegisterPassword('');
                setRegisterPasswordConfirm('');
                setRegisterFirstName('');
                setRegisterLastName('');
            } else {
                setAuthError(result.message || "Registrierung fehlgeschlagen.");
            }
        } catch (err: any) {
            setAuthError(err.message || "Ein Fehler ist aufgetreten.");
        } finally {
            setIsAuthLoading(false);
        }
    };

    const handleLogout = () => {
        setAuthUser(null);
        // Hier würdest du auch Tokens etc. löschen, falls verwendet
    };

    // Wenn Benutzer nicht angemeldet ist, zeige Login/Registrierung
    if (!authUser) {
        return (
            <div style={{ maxWidth: '400px', margin: '50px auto', padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
                {authError && <p style={{ color: 'red' }}>{authError}</p>}
                {showRegisterForm ? (
                    <form onSubmit={handleRegisterSubmit}>
                        <h2>Registrieren</h2>
                        <input type="email" value={registerEmail} onChange={(e) => setRegisterEmail(e.target.value)} placeholder="E-Mail" required style={{ width: '100%', padding: '8px', marginBottom: '10px', boxSizing: 'border-box' }} />
                        <input type="text" value={registerFirstName} onChange={(e) => setRegisterFirstName(e.target.value)} placeholder="Vorname (optional)" style={{ width: '100%', padding: '8px', marginBottom: '10px', boxSizing: 'border-box' }} />
                        <input type="text" value={registerLastName} onChange={(e) => setRegisterLastName(e.target.value)} placeholder="Nachname (optional)" style={{ width: '100%', padding: '8px', marginBottom: '10px', boxSizing: 'border-box' }} />
                        <input type="password" value={registerPassword} onChange={(e) => setRegisterPassword(e.target.value)} placeholder="Passwort" required style={{ width: '100%', padding: '8px', marginBottom: '10px', boxSizing: 'border-box' }} />
                        <input type="password" value={registerPasswordConfirm} onChange={(e) => setRegisterPasswordConfirm(e.target.value)} placeholder="Passwort bestätigen" required style={{ width: '100%', padding: '8px', marginBottom: '10px', boxSizing: 'border-box' }} />
                        <button type="submit" disabled={isAuthLoading} style={{ width: '100%', padding: '10px', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                            {isAuthLoading ? 'Registriere...' : 'Registrieren'}
                        </button>
                        <p style={{ textAlign: 'center', marginTop: '10px' }}>
                            Schon einen Account? <button type="button" onClick={() => setShowRegisterForm(false)} style={{ background: 'none', border: 'none', color: '#007bff', cursor: 'pointer', textDecoration: 'underline' }}>Einloggen</button>
                        </p>
                    </form>
                ) : (
                    <form onSubmit={handleLoginSubmit}>
                        <h2>Login</h2>
                        <input type="email" value={loginEmail} onChange={(e) => setLoginEmail(e.target.value)} placeholder="E-Mail" required style={{ width: '100%', padding: '8px', marginBottom: '10px', boxSizing: 'border-box' }} />
                        <input type="password" value={loginPassword} onChange={(e) => setLoginPassword(e.target.value)} placeholder="Passwort" required style={{ width: '100%', padding: '8px', marginBottom: '10px', boxSizing: 'border-box' }} />
                        <button type="submit" disabled={isAuthLoading} style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                            {isAuthLoading ? 'Logge ein...' : 'Login'}
                        </button>
                        <p style={{ textAlign: 'center', marginTop: '10px' }}>
                            Noch keinen Account? <button type="button" onClick={() => setShowRegisterForm(true)} style={{ background: 'none', border: 'none', color: '#007bff', cursor: 'pointer', textDecoration: 'underline' }}>Registrieren</button>
                        </p>
                    </form>
                )}
            </div>
        );
    }

    // Wenn Benutzer angemeldet ist, zeige eine Willkommensnachricht und den Logout-Button
    return (
        <div>
            <div style={{ marginBottom: '20px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h1>Willkommen, {authUser.firstName || authUser.email}!</h1>
                <button onClick={handleLogout} style={{ padding: '8px 15px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                    Logout
                </button>
            </div>
            <p>Du bist erfolgreich eingeloggt. Nutze die Navigation, um zu deinen Tasks oder Teams zu gelangen.</p>
        </div>
    );
}
