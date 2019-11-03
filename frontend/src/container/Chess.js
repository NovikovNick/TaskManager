import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

import * as Store from "../store/ReduxActions";

import {DndProvider, useDrag, useDrop} from 'react-dnd'
import HTML5Backend from 'react-dnd-html5-backend'

function renderSquare(i, [knightX, knightY], actions) {
    const x = i % 8
    const y = Math.floor(i / 8)
    const isKnightHere = x === knightX && y === knightY
    const black = (x + y) % 2 === 1
    const piece = isKnightHere ? <Knight/> : null

    return (
        <div key={i} style={{width: '12.5%', height: '12.5%'}}>
            <Square x={x} y={y} black={black} actions={actions}>{piece}</Square>
        </div>
    )
}

const ItemTypes = {
    KNIGHT: 'knight',
}

function Square({ x, y, black, children, actions}) {

    const fill = black ? 'black' : 'white'
    const stroke = black ? 'white' : 'black'

    const [{ isOver }, drop] = useDrop({
        accept: ItemTypes.KNIGHT,
        drop: () => actions.moveKnight(x, y),
        collect: monitor => ({
            isOver: !!monitor.isOver(),
        }),
    })

    return (
        <div
            ref={drop}
            style={{
                backgroundColor: isOver ? "yellow" : fill,
                color: stroke,
                width: '100%',
                height: '100%',
            }}
        >
            {children}

        </div>
    )
}

function Knight() {

    const [{isDragging}, drag] = useDrag({
        item: {type: ItemTypes.KNIGHT},
        collect: monitor => ({
            isDragging: !!monitor.isDragging(),
        }),
    })

    return (
        <div
            ref={drag}
            style={{
                opacity: isDragging ? 0.5 : 1,
                fontSize: 50,
                fontWeight: 'bold',
                cursor: 'move',
            }}
        >
            ♘
        </div>
    )
}

class Chess extends Component {

    constructor(props) {
        super(props);
        this.state = {...props};
    }

    start = () => {
        const randPos = () => Math.floor(Math.random() * 8)

        this.state.actions.moveKnight(randPos(), randPos())
    }

    render() {

        const {chess, actions} = this.props;

        const squares = []
        for (let i = 0; i < 64; i++) {
            squares.push(renderSquare(i, chess, actions))
        }

        return (

            <div>

                <button onClick={this.start}>start</button>
                <DndProvider backend={HTML5Backend}>

                    <div>


                        <div style={{
                            width: '400px',
                            height: '400px',
                            display: 'block',
                        }}>

                            <div
                                style={{
                                    width: '100%',
                                    height: '100%',
                                    display: 'flex',
                                    flexWrap: 'wrap',
                                }}
                            >
                                {squares}

                            </div>
                        </div>
                    </div>

                </DndProvider>
            </div>
        )
    }
}

const mapStateToProps = state => ({
    chess: state.task.chess
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(Store, dispatch)
});

export default connect(mapStateToProps, mapDispatchToProps)(Chess);