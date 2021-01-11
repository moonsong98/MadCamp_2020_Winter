import { User } from "./user.model";
import { Model } from 'mongoose';
export declare class UsersService {
    private readonly userModel;
    constructor(userModel: Model<User>);
    private findUserbyPhone;
    private findUserById;
    getUserInfo(phoneNum: string): Promise<string>;
    getFriendsbyPhone(id: string): Promise<void>;
    addUser(id: string, phoneNum: string): Promise<User>;
    getUsers(): Promise<{
        id: string;
        phoneNum: string;
    }[]>;
}
