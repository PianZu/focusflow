import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import Link from "next/link";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "FocusFlow App",
  description: "Verwalten Sie Ihre Aufgaben und Teams effizient",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <nav style={{ padding: "1rem", borderBottom: "1px solid #eee", marginBottom: "1rem", backgroundColor: "#f8f9fa" }}>
          <ul style={{ listStyle: "none", display: "flex", justifyContent: "center", gap: "2rem", margin: 0, padding: 0 }}>
            <li>
              <Link href="/tasks" style={{ textDecoration: "none", color: "#007bff", fontWeight: "normal" }}>
                Tasks
              </Link>
            </li>
            <li>
              <Link href="/teams" style={{ textDecoration: "none", color: "#007bff", fontWeight: "normal" }}>
                Teams
              </Link>
            </li>
            {/* Optional: Link zur Hauptseite/Login-Seite, falls gewünscht */}
            <li>
              <Link href="/" style={{ textDecoration: "none", color: "black", fontWeight: "normal" }}>
                Login/Home
              </Link>
            </li>
            {/* Weitere Navigationslinks hier einfügen */}
          </ul>
        </nav>
        <main style={{ padding: "1rem", maxWidth: "1200px", margin: "0 auto" }}>
          {children}
        </main>
        <footer style={{ padding: "1rem", borderTop: "1px solid #eee", marginTop: "2rem", textAlign: "center", color: "#6c757d" }}>
          <p>&copy; {new Date().getFullYear()} FocusFlow App</p>
        </footer>
      </body>
    </html>
  );
}
