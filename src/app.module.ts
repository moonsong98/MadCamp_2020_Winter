import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose'
import { UsersModule } from './users/users.module'
import { GroupsModule } from './groups/groups.module'
import { EventsModule } from './events/events.module'


@Module({
  imports:[MongooseModule.forRoot('mongodb+srv://AtlasAdmin:admin@cluster0.xhpzp.mongodb.net/user?retryWrites=true&w=majority'), UsersModule, GroupsModule, EventsModule],
  controllers: [],
  providers: [],
})
export class AppModule {}
