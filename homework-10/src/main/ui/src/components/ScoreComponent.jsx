function ScoreComponent({ scoreCounts, avgScore }){

    let max = scoreCounts.reduce((a,b) => a + b, 0);

    return(
        <div>
            <div>Scores</div>
            <div className="score-cont">5&nbsp;<progress max={max} value={scoreCounts[4]}></progress>&nbsp;{scoreCounts[4]}</div>
            <div className="score-cont">4&nbsp;<progress max={max} value={scoreCounts[3]}></progress>&nbsp;{scoreCounts[3]}</div>
            <div className="score-cont">3&nbsp;<progress max={max} value={scoreCounts[2]}></progress>&nbsp;{scoreCounts[2]}</div>
            <div className="score-cont">2&nbsp;<progress max={max} value={scoreCounts[1]}></progress>&nbsp;{scoreCounts[1]}</div>
            <div className="score-cont">1&nbsp;<progress max={max} value={scoreCounts[0]}></progress>&nbsp;{scoreCounts[0]}</div>
            <div>Average: {avgScore}</div>
        </div>
    );
}

export default ScoreComponent;