import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose'
import { UsersModule } from './users/users.module'

@Module({
  imports:[MongooseModule.forRoot('mongodb+srv://AtlasAdmin:admin@cluster0.xhpzp.mongodb.net/user?retryWrites=true&w=majority'), UsersModule],
  controllers: [],
  providers: [],
})
export class AppModule {}
