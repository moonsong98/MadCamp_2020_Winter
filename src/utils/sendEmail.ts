import * as nodemailer from 'nodemailer';

export const sendEmail = async (email: string, link: string) => {
	const transporter = nodemailer.createTransport({
		host: 'smtp.sendgrid.net',
		port: 465,
		secure: true, // true for 465, false for other ports
		auth: {
			user: 'apikey', // generated ethereal user
			pass: process.env.SENDGRID_API_KEY, // generated ethereal password
		},
	});

	// send mail with defined transport object
	const info = await transporter.sendMail({
		from: 'madcamp3temp@gmail.com', // sender address
		to: email, // list of receivers
		subject: 'Hello ✔', // Subject line
		text: 'Hello world?', // plain text body
		html: `<b>Hello world?</b><a href="${link}">Confirm Email</a>`, // html body
	});

	console.log(`link is ${link}`);
	console.log('Message sent: %s', info.messageId);
	// Message sent: <b658f8ca-6296-ccf4-8306-87d57a0b4321@example.com>
};
