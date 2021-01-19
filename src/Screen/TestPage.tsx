/* eslint-disable react/jsx-props-no-spreading */
import React, { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import { useMutation, gql } from '@apollo/client';

const UPLOAD_FILE = gql`
	mutation($file: Upload!) {
		uploadFile(file: $file)
	}
`;

export default function Upload() {
	const [uploadFile] = useMutation(UPLOAD_FILE);
	const onDrop = useCallback(
		([file]) => {
			console.log(file);
			uploadFile({ variables: { file } });
		},
		[uploadFile]
	);
	const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });
	return (
		<div {...getRootProps()}>
			<input {...getInputProps()} />
			{isDragActive ? <p>DragActive</p> : <p>Nothing</p>}
		</div>
	);
}
