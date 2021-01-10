import { User } from "./user.model";
import { Model } from 'mongoose';
export declare class UsersService {
    private readonly userModel;
    constructor(userModel: Model<User>);
    addUser(id: string, phoneNum: string): Promise<User>;
}
