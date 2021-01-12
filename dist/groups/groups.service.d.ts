import { Group } from "./group.model";
import { Model } from 'mongoose';
export declare class GroupsService {
    readonly groupModel: Model<Group>;
    constructor(groupModel: Model<Group>);
    createGroup(groupName: string, groupId: string, participants: Array<string>): Promise<Group>;
    getGroups(): Promise<Group[]>;
    getGroupbyName(name: string): Promise<Group>;
}
