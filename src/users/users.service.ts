import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User, UserDocument } from './schemas/user.schema';
import { CreateUserInput, LoginInput } from './inputs/user.input';
import { sendEmail } from 'src/utils/sendEmail';
import { confirmEmailLink } from 'src/utils/confirmEmailLink';
import { getUserIdFromRedis } from '../utils/getUserIdFromRedis';

@Injectable()
export class UsersService {
	constructor(@InjectModel(User.name) private userModel: Model<UserDocument>) {}

	/* Create */
	async createUser(createUserInput: CreateUserInput): Promise<User> {
		const createdUser = new this.userModel({
			username: createUserInput.username,
			password: createUserInput.password,
			name: createUserInput.name,
			email: createUserInput.email,
			confirmed: false,
		});
		const user = await createdUser.save();
		await sendEmail(createdUser.email, await confirmEmailLink(user.id));
		return user;
	}

	/* Read */
	async readAllUsers(): Promise<User[]> {
		return this.userModel.find().exec();
	}

	async login(loginInput: LoginInput): Promise<boolean> {
		return (
			(await this.userModel.find({ username: loginInput.username, password: loginInput.password }).exec()).length == 1
		);
	}

	/* Update */
	async confirmUser(redisId: string) {
		const userId = await getUserIdFromRedis(redisId);
		if (userId == null) {
			return false;
		} else {
			console.log(`userId: ${userId}`);
			await this.userModel.updateOne({ _id: userId }, { $set: { confirmed: true } });
			return true;
		}
	}
	/* Delete */
	async deleteUser(_id: string): Promise<boolean> {
		return null != (await this.userModel.findByIdAndDelete({ _id }));
	}

	async deleteAllUser(): Promise<boolean> {
		return null != (await this.userModel.deleteMany({}));
	}
}
