import { UsersService } from "./users.service";
import { Request } from 'express';
export declare class UsersController {
    private readonly usersService;
    constructor(usersService: UsersService);
    addUser(req: Request): Promise<import("./user.model").User>;
    getUsers(): Promise<{
        id: string;
        phoneNum: string;
        groupList: string[];
        friendList: import("./user.model").User[];
    }[]>;
    getFriends(req: Request): Promise<import("./user.model").User[]>;
    getUserInfo(Id: string): Promise<string>;
    updateGroup(req: Request): Promise<void>;
    getGroupList(Id: string): Promise<string[]>;
}
