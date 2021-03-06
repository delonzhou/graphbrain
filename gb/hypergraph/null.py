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


from gb.hypergraph.backend import Backend


class Null(Backend):
    """Hypergraph low-level operations."""

    def __init__(self):
        Backend.__init__(self)

    def close(self):
        pass

    def name(self):
        return 'Null hypergraph (does nothing, just for testing purposes)'

    def exists(self, vertex):
        """Checks if the given edge exists in the hypergraph."""
        return False

    def add(self, edge, timestamp=-1):
        """Adds an edges to the hypergraph if it does not exist yet."""
        return edge

    def remove(self, edge):
        """Removes an edges from the hypergraph."""
        pass

    def pattern2edges(self, pattern):
        """Return all the edges that match a pattern.
        A pattern is a collection of entity ids and wildcards (None)."""
        return set()

    def star(self, center, limit=None):
        """Return all the edges that contain a given entity.
        Entity can be atomic or an edge."""
        return set()

    def symbols_with_root(self, root):
        """Find all symbols with the given root."""
        return set()

    def edges_with_symbols(self, symbols, root=None):
        """Find all edges containing the given symbols, and optionally a given root"""
        return set()

    def destroy(self):
        """Erase the hypergraph."""
        pass

    def set_attribute(self, vertex, attribute, value):
        """Sets the value of an attribute."""
        return False

    def inc_attribute(self, vertex, attribute):
        """Increments attribute of a vertex."""
        return False

    def dec_attribute(self, vertex, attribute):
        """Decrements attribute of a vertex."""
        return False

    def get_str_attribute(self, vertex, attribute, or_else=None):
        """Returns attribute as string."""
        return or_else

    def get_int_attribute(self, vertex, attribute, or_else=None):
        """Returns attribute as integer value."""
        return or_else

    def get_float_attribute(self, vertex, attribute, or_else=None):
        """Returns attribute as float value."""
        return or_else

    def degree(self, vertex):
        """Returns the degree of a vertex."""
        return 0

    def timestamp(self, vertex):
        """Returns the timestamp of a vertex."""
        return -1

    def all(self):
        """Returns a lazy sequence of all the vertices in the hypergraph."""
        return []

    def all_attributes(self):
        """Returns a lazy sequence with a tuple for each vertex in the hypergraph.
           The first element of the tuple is the vertex itself,
           the second is a dictionary of attribute values (as strings)."""
        return []

    def symbol_count(self):
        """Total number of symbols in the hypergraph"""
        return 0

    def edge_count(self):
        """Total number of edge in the hypergraph"""
        return 0

    def total_degree(self):
        """Total degree of the hypergraph"""
        return 0
