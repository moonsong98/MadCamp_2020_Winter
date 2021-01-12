import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose'
import { UsersModule } from './users/users.module'
import { GroupsModule } from './groups/groups.module'


@Module({
  imports:[MongooseModule.forRoot('mongodb+srv://AtlasAdmin:admin@cluster0.xhpzp.mongodb.net/user?retryWrites=true&w=majority'), UsersModule, GroupsModule],
  controllers: [],
  providers: [],
})
export class AppModule {}
