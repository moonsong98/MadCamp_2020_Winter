import React, { useEffect, useState } from 'react';
import { BrowserRouter, Route, Link } from 'react-router-dom';
import axios from 'axios';
import Row from '../components/App/Row';
import PostDetail from './Post';
import MediaCard from '../components/App/Card';
import PrimarySearchAppBar from '../components/App/AppBar';
import PostPrices from './PostPrices';
import PriceDetail from './PriceDetail';
import CreatePrice from './CreatePrice';
import LoginPage from '../components/Auth/LoginPage';
import RegisterPage from '../components/Auth/RegisterPage';
import ConfirmPage from '../components/Auth/ConfirmPage';
import Upload from './TestPage';

import useAuthToken from '../config/auth';

/* 기획전 이름 / 설명  */
const dummyCards: Event[] = [
	{
		title: '캠핑에 감성을 더하다',
		content: '감성 캠핑용품 초특가 할인전',
		image: 'http://192.249.18.238:5000/post/getExhibitionImage/1.jpeg',
	},
	{
		title: '쉐프 라면 먹고 갈래?',
		content: '당신의 3분을 책임질 라면 컬렉션',
		image: 'http://192.249.18.238:5000/post/getExhibitionImage/2.jpeg',
	},
	{
		title: '얼어죽어도 포기할 수 없는 패션',
		content: '빈 옆구리를 따뜻하게 만들어 줄 옷을 골라',
		image: 'http://192.249.18.238:5000/post/getExhibitionImage/3.png',
	},
	{
		title: '저기압일 땐 고기앞으로',
		content: '특 A등급 소고기가 집 앞으로',
		image: 'http://192.249.18.238:5000/post/getExhibitionImage/4.jpeg',
	},
	{
		title: '코딩의 시작은 장비부터',
		content: '장인도 장비탓 한다',
		image: 'http://192.249.18.238:5000/post/getExhibitionImage/5.jpeg',
	},
];

const dummyHomes: Event[] = [
	{
		title: 'Chef',
		content: 'Chef',
		image:
			'https://lh3.googleusercontent.com/proxy/t-uyLVyxMdk-ApRakZZ8uEd_gfTi8JoQiIEvYx4ElxeF1FzXBnp0nC_8EG-e8tKHcbhotAfoGrDLcNK9iu-sxYntS2czwq7epS3Ts8z7OQA50rGJlurQ-0ME4eqjL9wv5cYA9TPTH3zrZwEi47tzWOPyy2uQ1sjLWsstdZXDH83khNfSzV4',
	},
	{
		title: 'Coco',
		content: 'Coco',
		image:
			'https://m.media-amazon.com/images/M/MV5BYjQ5NjM0Y2YtNjZkNC00ZDhkLWJjMWItN2QyNzFkMDE3ZjAxXkEyXkFqcGdeQXVyODIxMzk5NjA@._V1_.jpg',
	},
	{
		title: '범죄도시',
		content: '범죄도시',
		image:
			'https://lh3.googleusercontent.com/proxy/sQ4gZrtGjoFI6eGU8e_XMR_5K5BxImQKfXeaD5m93hbLNn6EnxjzXqbfXLTZ9wSH32gjKhTGx8ed9BFlg_L2Pel9Z0Y',
	},
];

type Movie = { originalName: string; name: string; posterPath: string };

type Event = { title: string; content: string; image: string };

function App() {
	// const url = 'http://192.249.18.238:5000';
	// const [state, setState] = useState({ source: '' });
	// useEffect(() => {
	// 	axios
	// 		.get(`${url}/post/getPostImage/Screen Shot 2021-01-20 at 6.31.29.png`, {
	// 			responseType: 'arraybuffer',
	// 		})
	// 		.then((response) => {
	// 			const base64 = btoa(new Uint8Array(response.data).reduce((data, byte) => data + String.fromCharCode(byte), ''));
	// 			setState({ source: `data:;base64,${base64}` });
	// 			dummyCards[0].image = state.source;
	// 			console.log(dummyCards[0].image);
	// 		});
	// }, [state.source]);
	// 외부 url에서 사진 가져오려면 Row에 fetchUrl props 추가해서 넣어주면 됨

	const [events, setEvents] = useState<Event[]>(dummyCards);

	const [homes, setHomes] = useState<Event[]>(dummyHomes);

	return (
		<div className="App">
			<BrowserRouter>
				<PrimarySearchAppBar />
				<switch>
					<Route exact path="/">
						<Row title="기획전" rowMovies={events} directTo="/prices" />
						<Row title="Start-UP Homes" rowMovies={homes} directTo="/details" />
					</Route>
					<Route path="/details">
						<PostDetail />
					</Route>
					<Route path="/prices">
						<PostPrices />
					</Route>
					<Route path="/pricedetail">
						<PriceDetail />
					</Route>
					<Route path="/createprice">
						<CreatePrice />
					</Route>
					<Route exact path="/login">
						<LoginPage />
					</Route>
					<Route path="/register" component={RegisterPage} />
					<Route path="/signup/confirm/:redisId" component={ConfirmPage} />
					<Route path="/test" component={Upload} />
				</switch>
			</BrowserRouter>
		</div>
	);
}

export default App;
