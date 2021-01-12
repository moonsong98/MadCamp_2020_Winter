import { User } from "./user.model";
import { Model } from 'mongoose';
export declare class UsersService {
    readonly userModel: Model<User>;
    constructor(userModel: Model<User>);
    getUserInfobyPhone(phoneNum: string): Promise<User>;
    getUserInfobyId(userId: string): Promise<string>;
    getFriends(userId: string, phoneList: Array<string>): Promise<User>;
    addUser(userId: string, name: string, phoneNum: string): Promise<User>;
    getUsers(): Promise<{
        id: string;
        name: string;
        phoneNum: string;
        groupList: string[];
        friendList: User[];
        eventList: string[];
    }[]>;
    updateUsersEvent(usersPhoneNumbers: Array<string>, eventName: string): Promise<void>;
    updateUsersGroupId(usersPhoneNumbers: Array<string>, groupName: string): Promise<void>;
    getGroupListbyId(Id: string): Promise<string[]>;
    getEventListbyId(Id: string): Promise<string[]>;
    private findUserbyPhone;
    private findUserById;
}
