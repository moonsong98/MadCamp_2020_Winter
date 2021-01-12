import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose'
import { UsersModule } from './users/users.module'
import { GroupsModule } from './groups/groups.module'


@Module({
  imports:[MongooseModule.forRoot('mongodb+srv://inhwa:inhwa@madcamp.yaarc.mongodb.net/users?retryWrites=true&w=majority'), UsersModule, GroupsModule],
  controllers: [],
  providers: [],
})
export class AppModule {}
