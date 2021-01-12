import { Group } from "./group.model";
import { Model } from 'mongoose';
import { User } from '../users/user.model';
export declare class GroupsService {
    readonly groupModel: Model<Group>;
    readonly userModel: Model<User>;
    constructor(groupModel: Model<Group>, userModel: Model<User>);
    createGroup(groupId: string, participants: Array<string>): Promise<Group>;
    getGroups(): Promise<{
        id: string;
        participants: User[];
    }[]>;
}
