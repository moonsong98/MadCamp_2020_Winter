import { UsersService } from "./users.service";
import { Request } from 'express';
export declare class GroupsController {
    private readonly groupsService;
    constructor(groupsService: UsersService);
    createGroup(req: Request): Promise<any>;
    getGroups(): Promise<any>;
}
