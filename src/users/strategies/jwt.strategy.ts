import { UsersService } from './../users.service';
import { PassportStrategy } from '@nestjs/passport';
import { Strategy, ExtractJwt } from 'passport-jwt';
import { Injectable, UnauthorizedException } from '@nestjs/common';
import { JwtDto } from '../dto/jwt.dto';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
	constructor(private usersService: UsersService) {
		super({
			jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
			secretOrKey: 'MAD-CAMP-123-3',
		});
	}
	async validate(payload: { userId: string; ita: string }) {
		const user = await this.usersService.validateUser(payload.userId);

		if (!user) {
			throw new UnauthorizedException();
		}

		return user;
	}
}
