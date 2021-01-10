import { User } from "./user.model";
import { Model } from 'mongoose';
export declare class UsersService {
    private readonly userModel;
    constructor(userModel: Model<User>);
    userHello(): string;
    addUser(id: string, phoneNum: string): Promise<User>;
    addUser1(id: string, phoneNum: string): Promise<User>;
    getUsers(): Promise<{
        id: string;
        phoneNum: string;
    }[]>;
}
