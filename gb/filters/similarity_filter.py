#   Copyright (c) 2016 CNRS - Centre national de la recherche scientifique.
#   All rights reserved.
#
#   Written by Telmo Menezes <telmo@telmomenezes.com>
#
#   This file is part of GraphBrain.
#
#   GraphBrain is free software: you can redistribute it and/or modify
#   it under the terms of the GNU Affero General Public License as published by
#   the Free Software Foundation, either version 3 of the License, or
#   (at your option) any later version.
#
#   GraphBrain is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU Affero General Public License for more details.
#
#   You should have received a copy of the GNU Affero General Public License
#   along with GraphBrain.  If not, see <http://www.gnu.org/licenses/>.


import operator
import gb.hypergraph.hypergraph as hyperg
import gb.hypergraph.symbol as sym
import gb.hypergraph.edge as ed
import gb.nlp.parser as par
from gb.nlp.enrich_edge import enrich_edge
import gb.metrics.word2vec_similarity as simil


EXCLUDE_RELS = ['are_synonyms/gb', 'src/gb', 'have_same_lemma/gb']


def exclude(edge):
    if sym.is_edge(edge):
        rel = edge[0]
        if sym.is_edge(rel):
            return False
        return rel in EXCLUDE_RELS
    else:
        return True


def write_edge_data(edge_data, file_path):
    f = open(file_path, 'w')
    for e in edge_data:
        # f.write('%s\n' % json.dumps(e, separators=(',', ':')))
        f.write('%s\n' % str(e['sim']))
        f.write('%s\n' % e['text'])
        f.write('%s\n' % ed.edge2str(ed.without_namespaces(ed.str2edge(e['edge']))))
    f.close()


class SimilarityFilter(object):
    def __init__(self, hg, parser, sim_threshold=.7):
        self.hg = hg
        self.parser = parser
        self.sim_threshold = sim_threshold

    def similar_edges(self, targ_edge):
        edges = self.hg.all()

        targ_eedge = enrich_edge(self.parser, targ_edge)

        sims = {}
        for edge in edges:
            if edge != targ_edge and not exclude(edge):
                eedge = enrich_edge(self.parser, edge)
                total_sim = simil.eedge_similarity(targ_eedge, eedge)
                if total_sim >= self.sim_threshold:
                    sims[ed.edge2str(edge)] = total_sim

        sorted_edges = sorted(sims.items(), key=operator.itemgetter(1), reverse=True)

        result = []
        for e in sorted_edges:
            edge_data = {'edge': e[0],
                         'sim': e[1],
                         'text': self.hg.get_str_attribute(ed.str2edge(e[0]), 'text')}
            result.append(edge_data)
        return result

    def edges_with_similar_concepts(self, targ_edge):
        edges = self.hg.all()

        targ_eedge = enrich_edge(self.parser, targ_edge)

        sims = {}
        for edge in edges:
            if edge != targ_edge and not exclude(edge):
                eedge = enrich_edge(self.parser, edge)
                total_sim, worst_sim, complete, matches = simil.edge_concepts_similarity(targ_eedge, eedge)
                if complete and worst_sim >= self.sim_threshold:
                    sims[ed.edge2str(edge)] = (worst_sim, total_sim, matches)

        sorted_edges = sorted(sims.items(), key=operator.itemgetter(1), reverse=True)

        result = []
        for e in sorted_edges:
            edge_data = {'edge': e[0],
                         'worst_sim': e[1][0],
                         'sim': e[1][1],
                         'matches': e[1][2],
                         'text': self.hg.get_str_attribute(ed.str2edge(e[0]), 'text')}
            result.append(edge_data)
        return result

    def write_similar_edges(self, targ_edge, file_path):
        edge_data = self.similar_edges(targ_edge)
        write_edge_data(edge_data, file_path)

    def write_edges_with_similar_concepts(self, targ_edge, file_path):
        edge_data = self.edges_with_similar_concepts(targ_edge)
        write_edge_data(edge_data, file_path)


if __name__ == '__main__':
    hgr = hyperg.HyperGraph({'backend': 'leveldb', 'hg': 'reddit-politics.hg'})

    print('creating parser...')
    par = par.Parser()
    print('parser created.')

    te = '(clinches/nlp.clinch.verb clinton/nlp.clinton.noun ' \
         '(+/gb democratic/nlp.democratic.adj nomination/nlp.nomination.noun))'

    s = SimilarityFilter(hgr, par)
    s.write_edges_with_similar_concepts(ed.str2edge(te), 'edges_similar_concepts.json')
    # s.write_similar_edges(ed.str2edge(te), 'similar_edges.json')
