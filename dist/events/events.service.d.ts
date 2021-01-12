import { Event } from "./event.model";
import { Model } from 'mongoose';
export declare class EventsService {
    readonly eventModel: Model<Event>;
    constructor(eventModel: Model<Event>);
    createEvent(name: string, date: string, members: Array<string>): Promise<Event>;
    getAllEvents(): Promise<Event[]>;
    getEvents(userId: string): Promise<void>;
}
