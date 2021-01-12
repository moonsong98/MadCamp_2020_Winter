import { EventsService } from "./events.service";
import { Request } from 'express';
export declare class EventsController {
    private readonly eventsService;
    constructor(eventsService: EventsService);
    createEvent(req: Request): Promise<import("./event.model").Event>;
    getAllEvents(): Promise<import("./event.model").Event[]>;
}
