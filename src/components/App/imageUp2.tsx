import React, { useState } from 'react';
import { DropzoneArea } from 'material-ui-dropzone';

function DropzoneAreaExample() {
	const [state, setState] = useState({
		files: [],
	});

	function handleChange(files) {
		setState({
			...state,
			files,
		});
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
