function init () {
    const tbody = document.getElementById('tableBody');
    let cells = [];
	let timer;

    document.getElementById('exec').addEventListener('click', function (){
        tbody.innerHTML ='';
        document.getElementById('result').textContent = '';
        document.getElementById('scoreboard').textContent = '';
        cells = [];
        clearInterval(timer);
        let gameEnded = false;
        let numClickedCell = 0;

        const numX = parseInt(document.getElementById('horizontalArray').value);
        const numY = parseInt(document.getElementById('verticalArray').value);
        const numMine = parseInt(document.getElementById('numOfMine').value);
        let numFlagged = 0;

		let sec = 0;
        function pad ( val ) { return val > 9 ? val : "0" + val; }
        timer = setInterval(function (){
            document.getElementById('seconds').innerHTML=pad(++sec%60);
            document.getElementById('minutes').innerHTML=pad(parseInt(sec/60,10));
        }, 1000);

        const score = document.getElementById('scoreboard');
        score.textContent = numMine;
		
        for (let i=0;i<numX;i++) {
            const xColumnCells = [];
            cells.push(xColumnCells);
            const tr = document.createElement('tr');

            for(let j=0;j<numY;j++) {
                xColumnCells.push(0);
                const td = document.createElement('td');
                td.addEventListener('contextmenu', function(e) {
                    if (gameEnded) {
                        return;
                    }
                    e.preventDefault();

                    if (e.currentTarget.textContent == '') {
                        e.currentTarget.classList.add('flagged');
                        e.currentTarget.textContent = '!';
                        numFlagged++;
                    } else if (e.currentTarget.textContent == '!') { 
                        e.currentTarget.textContent = '';
                        e.currentTarget.classList.remove('flagged');
                        numFlagged--;
                    }

                    let numMineLeft = numMine - numFlagged;
                    score.textContent = numMineLeft;
                })
                
                td.addEventListener('click', function(e){
                    if (gameEnded) {
                        return;
                    }

                    const trOfCurrentTarget = e.currentTarget.parentNode;
                    const tbodyOfCurrentTarget = e.currentTarget.parentNode.parentNode;
                    const yOfCurrentTarget = Array.prototype.indexOf.call(trOfCurrentTarget.children,e.currentTarget);
                    const xOfCurrentTarget = Array.prototype.indexOf.call(tbodyOfCurrentTarget.children,trOfCurrentTarget);

                    if (cells[xOfCurrentTarget][yOfCurrentTarget]==1||e.currentTarget.textContent == '!') {
                        return;
                    }

                    e.currentTarget.classList.add('opened');

                    if(cells[xOfCurrentTarget][yOfCurrentTarget]=='M') {
                        e.currentTarget.textContent = '펑';
                        e.currentTarget.classList.add('mined');
                        document.getElementById('result').textContent = 'FAIL';
                        gameEnded = true;
                        clearInterval(timer);
                    } else {
                        cells[xOfCurrentTarget][yOfCurrentTarget] = 1;
                        let neighborCells = 
                        [
                            cells[xOfCurrentTarget][yOfCurrentTarget-1], cells[xOfCurrentTarget][yOfCurrentTarget+1]
                        ];                
                        if (cells[xOfCurrentTarget-1]) {
                            neighborCells = neighborCells.concat([cells[xOfCurrentTarget-1][yOfCurrentTarget-1],cells[xOfCurrentTarget-1][yOfCurrentTarget],cells[xOfCurrentTarget-1][yOfCurrentTarget+1]]);
                        }  
                        if (cells[xOfCurrentTarget+1]) {
                            neighborCells = neighborCells.concat([cells[xOfCurrentTarget+1][yOfCurrentTarget-1],cells[xOfCurrentTarget+1][yOfCurrentTarget], cells[xOfCurrentTarget+1][yOfCurrentTarget+1]]);
                        }
                       let  neighborCellsMineNum = neighborCells.filter(function (v) {
                            return v === 'M';
                        }).length;
                        e.currentTarget.textContent = neighborCellsMineNum || ''; 

                        if (neighborCellsMineNum == 0) {
                            let neighborCells = [];   

                            if (tbody.children[xOfCurrentTarget-1]) {
                                neighborCells = neighborCells.concat([tbody.children[xOfCurrentTarget-1].children[yOfCurrentTarget-1],tbody.children[xOfCurrentTarget-1].children[yOfCurrentTarget],tbody.children[xOfCurrentTarget-1].children[yOfCurrentTarget+1]]);
                            }  

                            neighborCells.concat([tbody.children[xOfCurrentTarget].children[yOfCurrentTarget-1],tbody.children[xOfCurrentTarget].children[yOfCurrentTarget+1]]);
                            
                            if (tbody.children[xOfCurrentTarget+1]) {
                                neighborCells = neighborCells.concat([tbody.children[xOfCurrentTarget+1].children[yOfCurrentTarget-1],tbody.children[xOfCurrentTarget+1].children[yOfCurrentTarget], tbody.children[xOfCurrentTarget+1].children[yOfCurrentTarget+1]]);
                            }

                            neighborCells.filter(function (v) {
                                return !!v;
                            }).forEach(function(neighborCell) {
                                const trOfNeighborCell = neighborCell.parentNode;
                                const tbodyOfNeighborCell = neighborCell.parentNode.parentNode;
                                const yOfNeighborCell = Array.prototype.indexOf.call(trOfNeighborCell.children,neighborCell);
                                const xofNeighborCell = Array.prototype.indexOf.call(tbodyOfNeighborCell.children,trOfNeighborCell);

                                if (cells[xofNeighborCell][yOfNeighborCell] != 1) {
                                    neighborCell.click();
                                }
                            });

                        }
                        numClickedCell ++;

                        if (numClickedCell==numX*numY-numMine && numFlagged==numMine) {
                            gameEnded = true;
                            document.getElementById('result').textContent = 'SUCCESS';
                            clearInterval(timer);
                        }
                    }
                })
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }

        const randomMining = Array(numX*numY)
        .fill() 
        .map(function (e,idx) { 
            return idx;
        });
        const mineCellStorage = [];
        while(randomMining.length > numX*numY-numMine) {
            const mineCellNum = randomMining.splice(Math.floor(Math.random()*randomMining.length),1)[0];
            mineCellStorage.push(mineCellNum)
        }

        for (let i=0;i<mineCellStorage.length;i++) {
            const hor = Math.floor(mineCellStorage[i]/numX);
            const ver = mineCellStorage[i] % numX;
            cells[hor][ver] = 'M';
        }
    });
}

init();