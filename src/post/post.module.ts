import {Module} from '@nestjs/common'
import { MulterModule } from '@nestjs/platform-express';
import { PostController } from './post.controller';
import { PostService } from './post.service';
import { MongooseModule } from '@nestjs/mongoose';
import { PostSchema } from './post.model'
import { join } from 'path';


@Module({
    imports: [MongooseModule.forFeature([{name: 'Post', schema: PostSchema}]),MulterModule.register({dest:join(__dirname, '../images')})],
    controllers: [PostController],
    providers: [PostService],
    exports:[]
})
export class PostModule{}