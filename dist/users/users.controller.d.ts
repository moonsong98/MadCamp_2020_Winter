import { UsersService } from "./users.service";
import { HttpStatus } from '@nestjs/common';
import { Request } from 'express';
export declare class UsersController {
    private readonly usersService;
    constructor(usersService: UsersService);
    addUser(req: Request): Promise<{
        statusCode: HttpStatus;
        message: string;
    }>;
}
