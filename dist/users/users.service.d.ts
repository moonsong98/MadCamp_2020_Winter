import { User } from "./user.model";
import { Model } from 'mongoose';
export declare class UsersService {
    private readonly userModel;
    constructor(userModel: Model<User>);
    getUserInfobyPhone(phoneNum: string): Promise<User>;
    getUserInfobyId(userId: string): Promise<string>;
    getFriends(userId: string, phoneList: Array<string>): Promise<User>;
    addUser(userId: string, phoneNum: string): Promise<User>;
    getUsers(): Promise<{
        id: string;
        name: string;
        phoneNum: string;
    }[]>;
    private findUserbyPhone;
    private findUserById;
}
