// src/app/(app)/dashboard/page.tsx
"use client";

import React, { useEffect, useState } from 'react';
import Link from "next/link";
import { useUser } from '../../../context/sessionContext';
import { getTasksForUser, Task, TaskStatus } from '../../../services/taskService';
import { getTeamsForUser, Team } from '../../../services/teamService';

const formatDueDate = (dateString: string | undefined) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('de-DE', { day: 'numeric', month: 'short' });
};

const avatarColors = ["bg-sky-500", "bg-amber-500", "bg-violet-500", "bg-emerald-500", "bg-rose-500", "bg-indigo-500", "bg-fuchsia-500", "bg-teal-500"];
const getAvatarColors = (teamName: string) => {
    let hash = 0;
    for (let i = 0; i < teamName.length; i++) {
        hash = teamName.charCodeAt(i) + ((hash << 5) - hash);
    }
    const colors = [];
    for(let i = 0; i < 3; i++) {
        colors.push(avatarColors[(hash + i) % avatarColors.length]);
    }
    return colors;
};

const AvatarStack = ({ colors }: { colors: string[] }) => (
  <div className="flex -space-x-2">
    {colors.map((c, i) => (
      <div key={i} className={`h-8 w-8 rounded-full ring-2 ring-white ${c}`} />
    ))}
  </div>
);

const STATUS_STYLES: Record<TaskStatus, string> = {
  OPEN: "bg-emerald-100 text-emerald-800",
  PENDING: "bg-amber-100 text-amber-800",
  IN_REVIEW: "bg-sky-100 text-sky-800",
  CLOSED: "bg-zinc-200 text-zinc-600",
};

const StatusBadge: React.FC<{ status: TaskStatus }> = ({ status }) => (
  <span className={`rounded-full px-2.5 py-0.5 text-xs font-medium ${STATUS_STYLES[status]}`}>
    {status.replace("_", " ")}
  </span>
);

export default function DashboardPage() {
  const { user } = useUser();
  const [tasks, setTasks] = useState<Task[]>([]);
  const [teams, setTeams] = useState<Team[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (user?.id) {
      const fetchData = async () => {
        setIsLoading(true);
        try {
          const [userTasks, userTeams] = await Promise.all([
            getTasksForUser(user.id),
            getTeamsForUser(user.id),
          ]);
          
          setTeams(userTeams || []);
          
          // Wir filtern nicht mehr, da die API schon die richtigen Aufgaben liefert
          const openTasks = (userTasks || []).filter(
            t => t.status !== 'CLOSED'
          );
          
          setTasks(openTasks.slice(0, 4));

        } catch (error) {
          console.error("Failed to fetch dashboard data:", error);
          setTasks([]);
          setTeams([]);
        } finally {
          setIsLoading(false);
        }
      };
      fetchData();
    }
  }, [user?.id]);
  
  return (
    <>
      <header className="sticky top-0 z-10 flex h-16 items-center justify-between border-b border-zinc-200 bg-white/80 px-6 backdrop-blur-md">
        <div>
          <h1 className="text-xl font-bold">Dashboard</h1>
          <p className="text-sm text-zinc-500">Willkommen zurück, {user?.firstName || 'User'}!</p>
        </div>
        <div className="flex items-center gap-2">
          <Link href="/tasks" className="btn-primary">
            <PlusIcon className="h-4 w-4" />
            Neue Aufgabe
          </Link>
        </div>
      </header>

      <main className="flex-1 space-y-12 p-6 md:p-8">
        {isLoading ? (
            <p className="text-zinc-500">Lade Dashboard-Daten...</p>
        ) : (
            <>
                <section>
                  <div className="mb-6 flex items-center justify-between">
                    <h2 className="text-lg font-semibold">Anstehende Aufgaben</h2>
                    <Link href="/tasks" className="text-sm font-medium text-blue-600 hover:underline">
                      Alle anzeigen
                    </Link>
                  </div>
                  <div className="grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-4">
                    {tasks.map((t) => (
                      <article key={t.id} className="group rounded-2xl border border-zinc-200 bg-white p-5 shadow-sm transition-all hover:-translate-y-1 hover:shadow-md">
                        <div className="flex items-start justify-between gap-2">
                          <h3 className="font-medium leading-snug">{t.title}</h3>
                          <StatusBadge status={t.status || TaskStatus.OPEN} />
                        </div>
                        <div className="mt-4 flex items-center justify-between text-sm text-zinc-500">
                          <span className="inline-flex items-center gap-1.5">
                            <CalendarIcon className="h-4 w-4" />
                            Fällig {formatDueDate(t.dueDate)}
                          </span>
                          <span className={`inline-flex items-center gap-1.5 font-medium ${
                            t.priority === "HIGH" ? "text-red-500" : t.priority === "MEDIUM" ? "text-amber-500" : "text-emerald-500"
                          }`}>
                            <FlagIcon className="h-4 w-4" />
                            {t.priority}
                          </span>
                        </div>
                      </article>
                    ))}
                    {tasks.length === 0 && <p className="text-zinc-500 col-span-full">Keine anstehenden Aufgaben gefunden.</p>}
                  </div>
                </section>

                <section>
                  <div className="mb-6 flex items-center justify-between">
                    <h2 className="text-lg font-semibold">Meine Teams</h2>
                    <Link href="/teams" className="text-sm font-medium text-blue-600 hover:underline">
                      Teams verwalten
                    </Link>
                  </div>
                  <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
                    {teams.map((team) => (
                      <article key={team.id} className="group rounded-2xl border border-zinc-200 bg-white p-5 shadow-sm transition-all hover:-translate-y-1 hover:shadow-md">
                        <div className="flex items-center justify-between gap-2">
                          <h3 className="text-base font-medium">{team.name}</h3>
                          <AvatarStack colors={getAvatarColors(team.name)} />
                        </div>
                        <div className="mt-4 flex items-center justify-between text-sm text-zinc-500">
                          <span>{team.members?.length || 0} Mitglieder</span>
                          <Link href={`/teams/${team.id}`} className="inline-flex items-center gap-1 font-medium text-blue-600 hover:underline">
                            Anzeigen <ArrowRightIcon className="h-4 w-4" />
                          </Link>
                        </div>
                      </article>
                    ))}
                     {teams.length === 0 && <p className="text-zinc-500 col-span-full">Du bist in keinen Teams.</p>}
                  </div>
                </section>
            </>
        )}
      </main>
    </>
  );
}

const iconProps = { strokeWidth: 1.5, fill: "none", stroke: "currentColor" };
const PlusIcon = (p: React.SVGProps<SVGSVGElement>) => (<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" {...p}><path strokeLinecap="round" strokeLinejoin="round" d="M12 4.5v15m7.5-7.5h-15" {...iconProps} /></svg>);
const CalendarIcon = (p: React.SVGProps<SVGSVGElement>) => (<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" {...p}><path strokeLinecap="round" strokeLinejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0h18" {...iconProps} /></svg>);
const FlagIcon = (p: React.SVGProps<SVGSVGElement>) => (<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" {...p}><path strokeLinecap="round" strokeLinejoin="round" d="M3 3v1.5M3 21v-6m0 0 2.77-.693a9 9 0 0 1 6.208.682l.108.054a9 9 0 0 0 6.086.71l3.114-.732a48.524 48.524 0 0 1-.005-10.499l-3.11.732a9 9 0 0 1-6.085-.711l-.108-.054a9 9 0 0 0-6.208-.682L3 4.5M3 15V4.5" {...iconProps} /></svg>);
const ArrowRightIcon = (p: React.SVGProps<SVGSVGElement>) => (<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" {...p}><path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5 21 12m0 0-7.5 7.5M21 12H3" {...iconProps} /></svg>);