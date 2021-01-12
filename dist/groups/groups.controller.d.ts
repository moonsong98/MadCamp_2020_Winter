import { GroupsService } from "./groups.service";
import { Request } from 'express';
export declare class GroupsController {
    private readonly groupsService;
    constructor(groupsService: GroupsService);
    createGroup(req: Request): Promise<import("./group.model").Group>;
    getGroups(): Promise<{
        id: string;
        participants: import("../users/user.model").User[];
    }[]>;
}
