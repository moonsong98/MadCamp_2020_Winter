import { Group } from "./group.model";
import { Model } from 'mongoose';
import { User } from "src/users/user.model";
export declare class GroupsService {
    private readonly groupModel;
    private readonly userModel;
    constructor(groupModel: Model<Group>, userModel: Model<User>);
    createGroup(groupId: string, participants: Array<Group>): Promise<any>;
    getGroups(): Promise<{
        groupId: any;
        participants: any;
    }[]>;
}
