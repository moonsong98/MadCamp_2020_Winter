import { Module } from "@nestjs/common";
import { GroupsController} from './groups.controller';
import { GroupsService } from './groups.service';
import { MongooseModule } from '@nestjs/mongoose';
import { GroupSchema } from './group.model'
import { UsersModule } from "src/users/users.module";

@Module({
    imports:[MongooseModule.forFeature([{name: 'Group', schema: GroupSchema}]), UsersModule],
    controllers:[GroupsController],
    providers: [GroupsService],
})
export class GroupsModule{}