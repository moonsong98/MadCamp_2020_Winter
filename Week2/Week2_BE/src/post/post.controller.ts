import { Controller, Get, Post, Param, Req, Res, UploadedFile, UseInterceptors } from '@nestjs/common'
import { FileInterceptor } from '@nestjs/platform-express';
import { PostService } from './post.service';
import { diskStorage } from 'multer';
import { join } from 'path';
import { Response, Request } from 'express';

@Controller('post')
export class PostController {
    constructor(private readonly postService:PostService){}

    @Post('upload')
    @UseInterceptors(FileInterceptor('image', {
        storage: diskStorage({
            destination: join(__dirname, '../images'),
            filename: (req, file, cb) => { console.log(file.originalname); return cb(null, file.originalname) }
        })
    }))
    async getPhoto(@UploadedFile() file) {
        // console.log("@@@@@@@@@@@@@@@")
        console.log(file.originalname);
    }


    @Get(':filename')
    async test2(@Param() param: {filename: string}, @Res() res: Response) {
        console.dir(param)
        // console.log(typeof(filename))
        // console.log(typeof(String(filename)))
        // var st = String(filename)
        const filePath = join(__dirname, '..', 'images', param.filename);
        res.sendFile(filePath);
        // return File
    }
}