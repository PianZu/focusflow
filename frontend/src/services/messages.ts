import axios from "axios";
import { Message } from "@/types/message";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  timeout: 5000,
});

export async function getMessages(): Promise<Message[]> {
    try {
      const res = await api.get<Message[]>("/messages");
      return res.data;
    } catch (err) {
      console.error("Error fetching messages:", err);
      return [];
    }
  }
  