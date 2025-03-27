"use client";

import { Message } from "@/types/message";

type Props = {
  message: Message;
};

export default function MessageCard({ message }: Props) {
  return (
    <div className="p-4 border rounded-lg shadow mb-2">
      <p>{message.text}</p>
    </div>
  );
}
