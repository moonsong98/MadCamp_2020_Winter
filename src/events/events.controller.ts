import { EventsService } from "./events.service";
import { Controller, Get, Post, Put, Delete, Body, Param, Req ,Res, HttpStatus } from '@nestjs/common';
import { Request } from 'express'
import { ok } from "assert";

@Controller('events')  
export class EventsController{
    constructor(private readonly eventsService: EventsService){}
    @Post('postman')
    async createEvent(@Req()req:Request){
        console.log(req)
        const name = req.body.name
        const date = req.body.date
        const members = req.body.members
        console.log(name, date, members)
        const Event = await this.eventsService.createEvent(name, date, members)
        return Event
    }
    @Get('all')
    async getAllEvents(){
        const events = await this.eventsService.getAllEvents()
        return events
    }
    @Get('oneEvent/:name')
    async getOneEvent(@Param('name')name:string){
        const event = await this.eventsService.getEventByName(name)
        return event
    }
}