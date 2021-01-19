import React, { useState } from 'react';
import { useMutation, gql } from '@apollo/client';
import { Link } from 'react-router-dom';
import '../design/WritePage.css';
import { render } from '@testing-library/react';

const CREATE_POST = gql`
	mutation CreatePost($title: String!, $body: String!) {
		createPost(input: { title: $title, body: $body }) {
			id
			title
			body
		}
	}
`;

const UPLOAD_FILE = gql`
	mutation($file: Upload!) {
		uploadFile(file: $file)
	}
`;

interface WriteProps {
	movieList: Movie[];
	callBackfunc: (cbMoviesList: Movie[]) => void;
}

type Movie = { originalName: string; name: string; posterPath: string };

function WritePage(props: WriteProps) {
	const [uploadFile] = useMutation(UPLOAD_FILE);
	const [createPost, { data }] = useMutation(CREATE_POST);
	const [postDetail, setPostDetail] = useState({
		title: '',
		content: '',
	});
	const [previewImage, setPreviewImage] = useState({
		imgSrc: '',
	});
	const getValue = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setPostDetail({
			...postDetail,
			[name]: value,
		});
		console.log(postDetail);
	};
	const getContentData = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		const { name, value } = e.target;
		setPostDetail({
			...postDetail,
			[name]: value,
		});
		console.log(postDetail);
	};
	const addMovie = () => {
		console.log(`previewImage: ${previewImage}`);
		uploadFile({ variables: { previewImage } });
		// createPost({ variables: { title: postDetail.title, body: postDetail.content } });
		const newMovie = {
			originalName: 'Nully',
			name: 'Nully',
			posterPath: 'https://posterspy.com/wp-content/uploads/2020/03/shutter-island-genzo-1280.jpg',
		};
		props.callBackfunc(props.movieList.concat({ ...newMovie }));
	};
	const handleFileOnChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const reader = new FileReader();
		// console.log(e.target);
		// console.log(e.target.files);
		const tmpFiles: FileList | null = e.target.files;
		const file = tmpFiles?.item(0);
		uploadFile({ variables: { file } });

		// console.log(file);
		reader.onloadend = () => {
			if (reader.result != null) {
				const toString = reader.result.toString();
				const source = {
					imgSrc: toString,
				};
				console.log(reader.result);

				// uploadFile({ variables: { reader.result } });
				setPreviewImage(source);
			}
		};
		if (file != null) {
			reader.readAsDataURL(file);
		}
	};
	return (
		<div>
			<div className="form-wrapper">
				<input className="title-input" type="text" placeholder="제목" onChange={getValue} name="title" />
				<textarea className="text-area" placeholder="내용" onChange={getContentData} name="content" />
				<input
					className="image-input"
					type="file"
					accept="image/jpg, image/png, image/jpeg, image/gif"
					onChange={handleFileOnChange}
				/>
			</div>
			<Link to="/">
				<button type="submit" className="submit-button" onClick={addMovie}>
					입력
				</button>
			</Link>
			{previewImage.imgSrc && <img alt="error" className="previewImg" src={previewImage.imgSrc} />}
		</div>
	);
}

export default WritePage;
