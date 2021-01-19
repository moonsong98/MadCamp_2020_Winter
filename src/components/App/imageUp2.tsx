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

	return <DropzoneArea onChange={handleChange} />;
}

export default DropzoneAreaExample;
