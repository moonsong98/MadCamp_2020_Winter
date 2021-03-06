import { GqlAuthGuard } from './guards/gql-auth.guard';
import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { UsersResolver } from './users.resolver';
import { UsersService } from './users.service';
import { User, UserSchema } from './schemas/user.schema';
import { JwtStrategy } from './strategies/jwt.strategy';
import { JwtModule } from '@nestjs/jwt';

@Module({
	imports: [
		MongooseModule.forFeature([{ name: User.name, schema: UserSchema }]),
		JwtModule.register({
			secret: 'MAD-CAMP-123-3',
			signOptions: { expiresIn: '60m' },
		}),
	],
	providers: [UsersResolver, UsersService, JwtStrategy, GqlAuthGuard],
})
export class UsersModule {}
