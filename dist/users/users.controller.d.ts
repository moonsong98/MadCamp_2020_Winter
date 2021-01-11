import { UsersService } from "./users.service";
import { Request } from 'express';
export declare class UsersController {
    private readonly usersService;
    constructor(usersService: UsersService);
    addUser(req: Request): Promise<import("./user.model").User>;
    getUsers(): Promise<{
        id: string;
        phoneNum: string;
    }[]>;
    getUserInfo(id: string): Promise<string>;
}
