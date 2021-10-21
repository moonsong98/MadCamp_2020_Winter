/* eslint-disable react/jsx-props-no-spreading */
// import React, { useCallback } from 'react';
// import { useDropzone } from 'react-dropzone';
// import { useMutation, gql } from '@apollo/client';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

// const UPLOAD_FILE = gql`
// 	mutation($file: Upload!) {
// 		uploadFile(file: $file)
// 	}
// `;

// export default function Upload() {
// 	const [uploadFile] = useMutation(UPLOAD_FILE);
// 	const onDrop = useCallback(
// 		([file]) => {
// 			console.log(file);
// 			uploadFile({ variables: { file } });
// 		},
// 		[uploadFile]
// 	);
// 	const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });
// 	return (
// 		<div {...getRootProps()}>
// 			<input {...getInputProps()} />
// 			{isDragActive ? <p>DragActive</p> : <p>Nothing</p>}
// 		</div>
// 	);
// }

export default function ShowImage() {
	const url = 'http://192.249.18.238:5000';
	const [state, setState] = useState({ source: '' });
	useEffect(() => {
		axios
			.get(`${url}/post/getPostImage/IMG_2435-3e78.png`, {
				responseType: 'arraybuffer',
			})
			.then((response) => {
				const base64 = btoa(new Uint8Array(response.data).reduce((data, byte) => data + String.fromCharCode(byte), ''));
				setState({ source: `data:;base64,${base64}` });
				return (
					<div>
						<img alt="1" src={state.source} />
					</div>
				);
			});
	}, [state.source]);
	// this.setState({ source: "data:;base64," + base64 });

	return (
		<div>
			<img
				alt="1"
				src={
					state.source !== ''
						? state.source
						: 'https://scontent.ficn2-1.fna.fbcdn.net/v/t1.0-9/16174685_182158552261261_3740055036532727513_n.jpg?_nc_cat=103&ccb=2&_nc_sid=174925&_nc_ohc=bEKSUCl4mU4AX-C7BhQ&_nc_ht=scontent.ficn2-1.fna&oh=e47649209b7714cbad868251c01f9baa&oe=602E2688'
				}
			/>
		</div>
	);
}
