import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { MongooseModule } from '@nestjs/mongoose'

@Module({
  // imports:[MongooseModule.forRoot('mongodb+srv://inhwa:inhwa@madcamp.yaarc.mongodb.net/madcamp?retryWrites=true&w=majority')],
  imports:[MongooseModule.forRoot('mongodb://localhost:27017')],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
