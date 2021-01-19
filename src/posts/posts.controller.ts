import { CreatePostDTO, CreateCommentDTO } from './DTO/post.dto';
import { PostsService } from './posts.service';
import { Body, Controller, Get, Param, Post, Res, UploadedFile, UploadedFiles, UseInterceptors } from '@nestjs/common';
import { FileInterceptor, FilesInterceptor } from '@nestjs/platform-express';
import { diskStorage } from 'multer';
import { imageFileFilter, editFileName } from './util/file-upload.utils';
import { join } from 'path';
import { Response } from 'express';

@Controller('post')
export class PostsController {
	constructor(private postsService: PostsService) {}

	/* Create */
	@Post('test')
	@UseInterceptors(
		FileInterceptor('image', {
			storage: diskStorage({
				destination: './images',
				filename: (req, file, cb) => {
					console.log(file.originalname);
					return cb(null, file.originalname);
				},
			}),
		})
	)
	async test(@UploadedFile() file) {
		console.log(file);
		const response = {
			originalName: file.originalname,
			filename: file.filename,
		};
		console.log(response);
	}
	@Post('multiple')
	@UseInterceptors(
		FilesInterceptor('image', 20, {
			storage: diskStorage({
				destination: './files',
				filename: editFileName,
			}),
			fileFilter: imageFileFilter,
		})
	)
	async uploadMultipleFiles(@UploadedFiles() files) {
		const response = [];
		files.forEach((file) => {
			const fileReponse = {
				originalname: file.originalname,
				filename: file.filename,
			};
			response.push(fileReponse);
		});
		return response;
	}

	@Post('createpost')
	@UseInterceptors(
		FilesInterceptor('image', 20, {
			storage: diskStorage({
				destination: '/home/ubuntu/images',
				filename: editFileName,
			}),
			fileFilter: imageFileFilter,
		})
	)
	async createPost(@Body() createPostDTO: CreatePostDTO, @UploadedFiles() files) {
		const response = [];
		const fileNames = [];
		files.forEach((file) => {
			const fileReponse = {
				originalname: file.originalname,
				filename: file.filename,
			};
			fileNames.push(file.filename);
			response.push(fileReponse);
		});
		return await this.postsService.createPost(createPostDTO, fileNames);
	}

	@Post('createcomment')
	async createComment(@Body() createCommentDTO: CreateCommentDTO) {
		return await this.postsService.createComment(createCommentDTO);
	}

	/* Read */
	@Get('all')
	async readAllPosts() {
		return await this.postsService.findAll();
	}

	@Get('getPost/:id')
	async getPost(@Param('id') id: string) {
		return await this.postsService.getPost(id);
	}

	@Get('getPostImage/:id')
	async getPostImage(@Param('id') id: string, @Res() res: Response) {
		const filePath = join('/home/ubuntu/images', id);
		res.sendFile(filePath);
	}

	/* Update */

	/* Delete */
	@Get('deleteall')
	async deleteAllPosts() {
		return await this.postsService.deleteAllPosts();
	}
}
