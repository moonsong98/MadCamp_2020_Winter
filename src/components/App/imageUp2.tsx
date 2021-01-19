import React, { useState } from 'react';
import { DropzoneArea } from 'material-ui-dropzone';

interface dropzoneAreaExampleProps {
	callbackFunc: (files: []) => void;
}

function DropzoneAreaExample(props: dropzoneAreaExampleProps) {
	const { callbackFunc } = props;

	const [state, setState] = useState({
		files: [],
	});

	function handleChange(files) {
		setState({
			...state,
			files,
		});
		callbackFunc(files);
	}

	console.log(state);
	return (
		<DropzoneArea
			onChange={handleChange}
			showPreviews
			maxFileSize={5000000000}
			filesLimit={10}
			showPreviewsInDropzone={false}
		/>
	);
}

export default DropzoneAreaExample;
