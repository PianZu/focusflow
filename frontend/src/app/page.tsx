// src/app/page.tsx

"use client";

import { useEffect, useState } from 'react';
import { loginUser, UserLoginData, LoginResponse } from '@/services/authService';
import { registerUser, UserRegistrationData } from '@/services/userService';
import { UserProfile } from '@/services/authService';
import { useRouter} from 'next/navigation';

export default function MainPage() {
  // Authentifizierungs-State
  const [authUser, setAuthUser] = useState<UserProfile | null>(null);
  const [isAuthLoading, setIsAuthLoading] = useState(false);
  const [authError, setAuthError] = useState<string | null>(null);
  const [showRegisterForm, setShowRegisterForm] = useState(false);

  // Login-Form-State
  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');

  // Registrierungs-Form-State
  const [registerEmail, setRegisterEmail] = useState('');
  const [registerPassword, setRegisterPassword] = useState('');
  const [registerPasswordConfirm, setRegisterPasswordConfirm] = useState('');
  const [registerFirstName, setRegisterFirstName] = useState('');
  const [registerLastName, setRegisterLastName] = useState('');

  useEffect(() => {
    // Hier: Token/Session-Prüfung
  }, []);

  const handleLoginSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsAuthLoading(true);
    setAuthError(null);

    const credentials: UserLoginData = { email: loginEmail, password: loginPassword };
    try {
      const result: LoginResponse = await loginUser(credentials);
      if (result.success) {
        // Placeholder: später richtige Userdaten setzen
        setAuthUser({ id: Date.now(), email: loginEmail });
        setLoginEmail('');
        setLoginPassword('');
      } else {
        setAuthError(result.message);
      }
    } catch (err: any) {
      setAuthError(err.message || 'Ein Fehler ist aufgetreten.');
    } finally {
      setIsAuthLoading(false);
    }
  };

  const handleRegisterSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (registerPassword !== registerPasswordConfirm) {
      setAuthError('Passwörter stimmen nicht überein.');
      return;
    }
    setIsAuthLoading(true);
    setAuthError(null);

    const data: UserRegistrationData = {
      email: registerEmail,
      password: registerPassword,
      passwordConfirm: registerPasswordConfirm,
      firstName: registerFirstName,
      lastName: registerLastName,
    };

    try {
      const result = await registerUser(data);
      if (result.success) {
        alert(result.message);
        setShowRegisterForm(false);
        setRegisterEmail('');
        setRegisterPassword('');
        setRegisterPasswordConfirm('');
        setRegisterFirstName('');
        setRegisterLastName('');
      } else {
        setAuthError(result.message);
      }
    } catch (err: any) {
      setAuthError(err.message || 'Ein Fehler ist aufgetreten.');
    } finally {
      setIsAuthLoading(false);
    }
  };

  const handleLogout = () => {
    setAuthUser(null);
  };

  // Unangemeldet: Login oder Registrierung
  if (!authUser) {
    return (
      <div style={{ maxWidth: 400, margin: '50px auto', padding: 20, border: '1px solid #ccc', borderRadius: 8 }}>
        {authError && <p style={{ color: 'red' }}>{authError}</p>}
        {showRegisterForm ? (
          <form onSubmit={handleRegisterSubmit}>
            <h2>Registrieren</h2>
            <input type="email" value={registerEmail} onChange={e => setRegisterEmail(e.target.value)} placeholder="E-Mail" required style={{ width: '100%', padding: 8, marginBottom: 10 }} />
            <input type="text" value={registerFirstName} onChange={e => setRegisterFirstName(e.target.value)} placeholder="Vorname" style={{ width: '100%', padding: 8, marginBottom: 10 }} />
            <input type="text" value={registerLastName} onChange={e => setRegisterLastName(e.target.value)} placeholder="Nachname" style={{ width: '100%', padding: 8, marginBottom: 10 }} />
            <input type="password" value={registerPassword} onChange={e => setRegisterPassword(e.target.value)} placeholder="Passwort" required style={{ width: '100%', padding: 8, marginBottom: 10 }} />
            <input type="password" value={registerPasswordConfirm} onChange={e => setRegisterPasswordConfirm(e.target.value)} placeholder="Passwort bestätigen" required style={{ width: '100%', padding: 8, marginBottom: 10 }} />
            <button type="submit" disabled={isAuthLoading} style={{ width: '100%', padding: 10, backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
              {isAuthLoading ? 'Registriere…' : 'Registrieren'}
            </button>
            <p style={{ textAlign: 'center', marginTop: 10 }}>
              Schon einen Account?{' '}
              <button type="button" onClick={() => setShowRegisterForm(false)} style={{ background: 'none', border: 'none', color: '#007bff', cursor: 'pointer' }}>
                Einloggen
              </button>
            </p>
          </form>
        ) : (
          <form onSubmit={handleLoginSubmit}>
            <h2>Login</h2>
            <input type="email" value={loginEmail} onChange={e => setLoginEmail(e.target.value)} placeholder="E-Mail" required style={{ width: '100%', padding: 8, marginBottom: 10 }} />
            <input type="password" value={loginPassword} onChange={e => setLoginPassword(e.target.value)} placeholder="Passwort" required style={{ width: '100%', padding: 8, marginBottom: 10 }} />
            <button type="submit" disabled={isAuthLoading} style={{ width: '100%', padding: 10, backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
              {isAuthLoading ? 'Logge ein…' : 'Login'}
            </button>
            <p style={{ textAlign: 'center', marginTop: 10 }}>
              Noch keinen Account?{' '}
              <button type="button" onClick={() => setShowRegisterForm(true)} style={{ background: 'none', border: 'none', color: '#007bff', cursor: 'pointer' }}>
                Registrieren
              </button>
            </p>
          </form>
        )}
      </div>
    );
  }

  // Angemeldet
  return (
    <div style={{ padding: 20 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <h1>Willkommen, {authUser.firstName || authUser.email}!</h1>
        <button onClick={handleLogout} style={{ padding: '8px 15px', backgroundColor: '#dc3545', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
          Logout
        </button>
      </div>
      <p>Du bist erfolgreich eingeloggt. Nutze die Navigation, um zu deinen Tasks oder Teams zu gelangen.</p>
    </div>
  );
}
