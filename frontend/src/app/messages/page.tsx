import { getMessages } from "@/services/messages";
import MessageCard from "@/components/MessageCard";

export default async function MessagesPage() {
  const messages = await getMessages();

  return (
    <div className="max-w-xl mx-auto mt-10">
      <h1 className="text-2xl font-bold mb-4">Messages</h1>
      {messages.map((msg) => (
        <MessageCard key={msg.id} message={msg} />
      ))}
    </div>
  );
}
