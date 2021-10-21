import { UsersService } from "./users.service";
import { Request } from 'express';
export declare class UsersController {
    private readonly usersService;
    constructor(usersService: UsersService);
    addUser(req: Request): Promise<import("./user.model").User>;
    getUsers(): Promise<{
        id: string;
        name: string;
        phoneNum: string;
        groupList: string[];
        friendList: import("./user.model").User[];
        eventList: string[];
    }[]>;
    getFriends(req: Request): Promise<import("./user.model").User[]>;
    getUserInfo(Id: string): Promise<string>;
    getUserName(phoneNum: string): Promise<import("./user.model").User>;
    updateGroup(req: Request): Promise<void>;
    updateEvent(req: Request): Promise<void>;
    getGroupList(Id: string): Promise<string[]>;
    getEventList(Id: string): Promise<string[]>;
}
