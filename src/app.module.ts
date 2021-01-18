import { Module } from '@nestjs/common';
import { GraphQLModule } from '@nestjs/graphql';
import { join } from 'path';
import { MongooseModule } from '@nestjs/mongoose';
import { UsersModule } from './users/users.module';
import { PostsModule } from './posts/posts.module';
import { MulterModule } from '@nestjs/platform-express';

@Module({
	imports: [
		GraphQLModule.forRoot({
			autoSchemaFile: join(process.cwd(), 'schema.gql'),
		}),
		MongooseModule.forRoot(
			'mongodb+srv://AtlasAdmin:admin@cluster0.xhpzp.mongodb.net/test?retryWrites=true&w=majority'
		),
		MulterModule.register({
			dest: './uploads',
		}),
		UsersModule,
		PostsModule,
	],
})
export class AppModule {}
