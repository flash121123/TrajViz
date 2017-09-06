
TrajViz 1.0
==========

source code public repository. This code is released under [GPL v.2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html).

TrajViz 1.0 is a software for visualize motifs (frequently repeated subsequences) in spatial trajectories with GUI interfaces. The GUI enables user to interactive visualize with variable length patterns and anomalous discovery from time series

It is implemented in Java and is based on [SAX](https://github.com/jMotif/SAX) and a variation of Iterative Grammatical Inference with [ItrSequitur](). The Interactive Map Panel GUI is build on the top of Stepan Rutz's code.

In contrast with [GrammarViz 3.0](https://github.com/GrammarViz2/grammarviz2_src), the software is designed for detecting subsequence which *Spatially Similar*, which is different from traditional motif discovery. User can refer [GrammarViz 3.0](https://github.com/GrammarViz2/grammarviz2_src) for time series version motif based visualization software.

### References:

[1] Lin, J., Keogh, E., Wei, L. and Lonardi, S., [*Experiencing SAX: a Novel Symbolic Representation of Time Series*](http://cs.gmu.edu/~jessica/SAX_DAMI_preprint.pdf). [DMKD Journal](http://link.springer.com/article/10.1007%2Fs10618-007-0064-z), 2007.

[2] Nevill-Manning, C.G., Witten, I.H., *Identifying Hierarchical Structure in Sequences: A linear-time algorithm.* [arXiv:cs/9709102](http://arxiv.org/abs/cs/9709102), 1997.

[3] Yifeng Gao, Jessica Lin, Huzefa Rangwala. 2016. [Iterative Grammar-Based Framework for Discovering Variable-Length Time Series Motifs.](http://cs.gmu.edu/~jessica/publications/ItrSequitur_ICMLA16.pdf) In Proceedings of the 15th International Conference on Machine Learning and Applications (ICMLA). Anaheim, CA. December 18-20.* ICMLA 16.


### [Citing this work](https://scholar.googleusercontent.com/scholar.bib?q=info:5b639CBi3PcJ:scholar.google.com/&output=citation&scisig=AAGBfm0AAAAAWacbYLuvx2vlSC2DbXB68E1Qx_nAKTYn&scisf=4&ct=citation&cd=-1&hl=en&scfhb=1)

Yifeng Gao, Qingzhe Li, Xiaosheng Li, Jessica Lin, Huzefa Rangwala, TrajViz: A Tool for Visualizing Patterns and Anomalies in Trajectory, ECML/PKDD Conference, 2017




Software Instruction 
==========

For using the software directly, uncompressed TrajViz_beta.zip, and run TrajViz_beta.jar


Build Instruction (From Source Code)
==========

To build the project (In Eclipse)

1. Download the code and unzip.

2. Create a new project

3. Select "Import" then choose "File System"

4. Select all the unzip files.

5. Overwrite all files except .classpath

6. Select "Build Path" then choose "Add External JAR". 

7. Add all JAR in lib directory. 

8. Run "TrajVizGui" to see whether the program is correct.

Acknowledgement
==========

We would like to thank Ranjeev Mittu at the Naval Research Lab (NRL)
for the support and valuable suggestions on our work. 
