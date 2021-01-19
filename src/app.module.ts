import { PostsModule } from './posts/posts.module';
import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { MulterModule } from '@nestjs/platform-express/multer';

@Module({
	imports: [
		MongooseModule.forRoot(
			'mongodb+srv://AtlasAdmin:admin@cluster0.xhpzp.mongodb.net/test?retryWrites=true&w=majority'
		),
		MulterModule.register({
			dest: './images',
		}),
		PostsModule,
	],
})
export class AppModule {}
